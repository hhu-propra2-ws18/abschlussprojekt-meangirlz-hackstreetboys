package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.ReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@ComponentScan(basePackages = "de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service")
public class PropayManager {

    final String url = "http://propay:8888/";
    private final int maxVersuche = 3;
    private final int verzoegerung = 1000;

    RestTemplate rt;

    @Autowired
    public PropayManager(RestTemplate rt) {
        this.rt = rt;
    }

    /**
     * Gibt die Informationen zu einem Account zurück.
     * Wenn der Account noch nicht existiert, wird ein leerer Account angelegt.
     *
     * @param benutzername Gibt den Account namen an
     * @return
     */
    @Retryable(maxAttempts = maxVersuche, value = RuntimeException.class, backoff = @Backoff(delay = verzoegerung))
    public AccountDto getAccount(String benutzername) {
        try {
            ResponseEntity<AccountDto> result = rt.getForEntity(url + "account/" + benutzername, AccountDto.class);
            AccountDto acc = result.getBody();
            return acc;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Recover
    public AccountDto recoverNull(RuntimeException e) {
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
    @Retryable(maxAttempts = maxVersuche, value = RestClientException.class, backoff = @Backoff(delay = verzoegerung))
    public int guthabenAufladen(String benutzername, int anzahl) {

        ResponseEntity<AccountDto> result = rt.postForEntity(url
            + "account/"
            + benutzername
            + "?amount="
            + anzahl, null, AccountDto.class);
        result.getBody();

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
    @Retryable(maxAttempts = maxVersuche, value = HttpStatusCodeException.class,
        backoff = @Backoff(delay = verzoegerung))
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
     *              Null falls Propay nicht erreichbar ist
     */
    @Retryable(maxAttempts = maxVersuche, value = RuntimeException.class, backoff = @Backoff(delay = verzoegerung))
    public ReservationDto kautionReserviern(String vonBenutzername, String zuBenutzername, int anzahl) {

        String kautionUrl = url + "reservation/reserve/" + vonBenutzername + "/" + zuBenutzername + "?amount=" + anzahl;
        ReservationDto res;
        try {
            ResponseEntity<ReservationDto> result = rt.postForEntity(kautionUrl, null, ReservationDto.class);
            res = result.getBody();
            return res;
        } catch (RestClientException e) {
            int i = 500;
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException clientEx = ((HttpClientErrorException) e);
                i = clientEx.getRawStatusCode();
            }
            if (i < 500) {
                res = new ReservationDto();
                res.setId(-1);
                return res;
            } else {
                return null;
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
    @Retryable(maxAttempts = maxVersuche, value = RestClientException.class, backoff = @Backoff(delay = verzoegerung))
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
    @Retryable(maxAttempts = maxVersuche, value = RestClientException.class, backoff = @Backoff(delay = verzoegerung))
    public int kautionFreigeben(String benutzername, int reservationsId) {

        String kautionUrl = url + "reservation/release/" + benutzername + "?reservationId=" + reservationsId;
        return postAccountAnfrage(kautionUrl);
    }

    private int postAccountAnfrage(String anfrageUrl) throws RestClientException {
        rt.postForEntity(anfrageUrl, null, AccountDto.class);
        return 200;
    }

    /**
     * Faengt nach den Retries die Methode ab und gibt ein sicheren Wert zurueck.
     * @param e Exception.
     * @return Der sichere Wert.
     */
    @Recover
    public int recoverStatusCode(RestClientException e) {
        System.out.println("Recovering - returning safe value");
        if (e instanceof HttpClientErrorException) {
            HttpClientErrorException clientEx = ((HttpClientErrorException) e);
            return clientEx.getStatusCode().value();
        }
        return 500;
    }
}
