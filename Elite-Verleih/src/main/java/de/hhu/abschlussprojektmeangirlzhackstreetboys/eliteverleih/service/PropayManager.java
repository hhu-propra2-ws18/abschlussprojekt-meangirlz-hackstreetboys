package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.ReservationDto;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@ComponentScan(basePackages = "de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service")
public class PropayManager {

    private final String url = "http://localhost:8888/";
    private final int maxVersuche = 3;
    private final int verzoegerung = 1000;

    @Autowired
    AusleiheManager ausleiheM;

    @Autowired
    RestTemplate rt;

    /**
     * Gibt die Informationen zu einem Account zurück.
     * Wenn der Account noch nicht existiert, wird ein leerer Account angelegt.
     *
     * @param benutzername Gibt den Account namen an
     * @return
     */
    @Retryable(maxAttempts=maxVersuche,value=RuntimeException.class,backoff = @Backoff(delay = verzoegerung))
    public AccountDto getAccount(String benutzername) throws RuntimeException{
        try {
            ResponseEntity<AccountDto> result = rt.getForEntity(url + "account/" + benutzername, AccountDto.class);
            AccountDto acc = result.getBody();
            return acc;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Recover
    public AccountDto recoverNull(RuntimeException e){
        System.out.println("Recovering - returning safe value");
        return null;
    }

    /**
     * Wenn der Account noch nicht existiert, wird er angelegt.
     * Erhöht den Account um den Wert in anzahl.
     *
     * @param benutzername Ziel Accountname
     * @param anzahl       Summe die aufgeladen werden soll
     */
    @Retryable(maxAttempts=maxVersuche,value=HttpClientErrorException.class,backoff = @Backoff(delay = verzoegerung))
    public int guthabenAufladen(String benutzername, int anzahl) {

        ResponseEntity<AccountDto> result = rt.postForEntity(url
            + "account/"
            + benutzername
            + "?amount="
            + anzahl, null, AccountDto.class);
            AccountDto acc = result.getBody();
        return 200;
    }

    /**
     * Ueberweist von Quellaccount zu dem Zielaccount. Wenn der Quellaccount nicht genug Geld hat
     * wird nicht ueberwiesen und es wird ein false zurueck gegeben. Ansonsten true.
     *
     * @param vonBenutzername Quellaccountname
     * @param zuBenutzername  Zielaccountname
     * @param anzahl          zu ueberweisende Summe
     * @return boolean true wenn alles klappt und false bei einem Fehler
     */
    @Retryable(maxAttempts=maxVersuche,value=HttpClientErrorException.class,backoff = @Backoff(delay = verzoegerung))
    public int ueberweisen(String vonBenutzername, String zuBenutzername, int anzahl) {

        String ueberweisenUrl = url
            + "account/"
            + vonBenutzername
            + "/transfer/"
            + zuBenutzername
            + "?amount="
            + anzahl;
        return postAccountAnfrage(ueberweisenUrl);
    }

    /**
     * Die Kaution wird auf dem Quellaccount reserviert. Damit wird das nicht abgehoben sondern nur reserviert.
     * Also kann der Quellaccount nicht mehr sein Geld bis zum Kontostand 0 ausgeben sondern nur noch so viel das er
     * noch genug fuer die Kaution hat.
     *
     * @param vonBenutzername Quellaccount wo die Kaution reserviert wird
     * @param zuBenutzername  Zielaccount falls die Kaution eingezogen wird geht sie hier hin
     * @param anzahl          Summe der Kaution
     * @return ReservationDto mit den Daten, ResavationDTO mit Id -1 falls Propay fehlschlaegt,
     * Null falls Propay nicht erreichbar ist
     */
    @Retryable(maxAttempts=maxVersuche,value=RuntimeException.class,backoff = @Backoff(delay = verzoegerung))
    public ReservationDto kautionReserviern(String vonBenutzername, String zuBenutzername, int anzahl) {

        String kautionUrl = url + "reservation/reserve/" + vonBenutzername + "/" + zuBenutzername + "?amount=" + anzahl;
        ReservationDto res;
        try {
            ResponseEntity<ReservationDto> result = rt.postForEntity(kautionUrl, null, ReservationDto.class);
            res = result.getBody();
            return res;
        } catch (HttpClientErrorException e) {
            System.err.println(e.getStatusCode().value());
            if(e.getStatusCode().value() < 500){
                return null;
            }
            else{
                res = new ReservationDto();
                res.setId(-1);
                return res;
            }
        }
    }

    /**
     * Zieht dem Account das reservierte Guthaben ab und überträgt den reservierten Betrag
     * an den zuvor definierten Zielaccount.
     *
     * @param benutzername   Quellaccount
     * @param reservationsId Id der Reservation
     * @return 200 = OK, 400-499 = Account Fehler, ab 500 = Server down
     */
    @Retryable(maxAttempts=maxVersuche,value=HttpClientErrorException.class,backoff = @Backoff(delay = verzoegerung))
    public int kautionEinziehen(String benutzername, int reservationsId) {

        String kautionUrl = url + "reservation/punish/" + benutzername + "?reservationId=" + reservationsId;
        return postAccountAnfrage(kautionUrl);
    }

    /**
     * Loese eine Reservierung. In diesem Fall wird dem Account sein Guthaben wieder zur freien Verfügung gegeben
     * und die Reservierung wird restlos geloescht.
     *
     * @param benutzername   Quellaccount
     * @param reservationsId Id der Reservation
     * @return 200 = OK, 400-499 = Account Fehler, ab 500 = Server down
     */
    @Retryable(maxAttempts=maxVersuche,value=HttpClientErrorException.class,backoff = @Backoff(delay = verzoegerung))
    public int kautionFreigeben(String benutzername, int reservationsId) {

        String kautionUrl = url + "reservation/release/" + benutzername + "?reservationId=" + reservationsId;
        return postAccountAnfrage(kautionUrl);
    }

    private int postAccountAnfrage(String kautionUrl) throws HttpClientErrorException{
            rt.postForEntity(kautionUrl, null, AccountDto.class);
            return 200;
    }

    @Recover
    public int recoverStatusCode(HttpClientErrorException e){
        System.out.println("Recovering - returning safe value");
        return e.getStatusCode().value();
    }
}
