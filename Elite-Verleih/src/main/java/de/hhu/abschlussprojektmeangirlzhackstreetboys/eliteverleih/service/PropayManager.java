package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.ReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PropayManager {

    final String url = "http://localhost:8888/";

    RestTemplate rt = new RestTemplate();

    /**
     * Gibt die Informationen zu einem Account zurück.
     * Wenn der Account noch nicht existiert, wird ein leerer Account angelegt.
     *
     * @param benutzername Gibt den Account namen an
     * @return
     */
    public AccountDto getAccount(String benutzername) {

        ResponseEntity<AccountDto> result = rt.getForEntity(url + "account/" + benutzername, AccountDto.class);
        AccountDto acc = result.getBody();
        return acc;
    }

    /**
     * Wenn der Account noch nicht existiert, wird er angelegt.
     * Erhöht den Account um den Wert in anzahl.
     *
     * @param benutzername Ziel Accountname
     * @param anzahl       Summe die aufgeladen werden soll
     */
    public void guthabenAufladen(String benutzername, int anzahl) {

        ResponseEntity<AccountDto> result = rt.postForEntity(url
            + "account/"
            + benutzername
            + "?amount="
            + anzahl, null, AccountDto.class);
        AccountDto acc = result.getBody();

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
    public boolean ueberweisen(String vonBenutzername, String zuBenutzername, int anzahl) {

        String urlueberweisenUrl = url
            + "account/"
            + vonBenutzername
            + "/transfer/"
            + zuBenutzername
            + "?amount="
            + anzahl;
        try {
            rt.postForEntity(urlueberweisenUrl, null, AccountDto.class);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Die Kaution wird auf dem Quellaccount reserviert. Damit wird das nicht abgehoben sondern nur reserviert.
     * Also kann der Quellaccount nicht mehr sein Geld bis zum Kontostand 0 ausgeben sondern nur noch so viel das er
     * noch genug fuer die Kaution hat.
     *
     * @param vonBenutzername Quellaccount wo die Kaution reserviert wird
     * @param zuBenutzername  Zielaccount falls die Kaution eingezogen wird geht sie hier hin
     * @param anzahl          Summe der Kaution
     * @return ReservationDto ist ein Reservation Objekt welches die ReservationsId und einen Betrag beinhaltet
     */
    public ReservationDto kautionReserviern(String vonBenutzername, String zuBenutzername, int anzahl) {

        String kautionUrl = url + "reservation/reserve/" + vonBenutzername + "/" + zuBenutzername + "?amount=" + anzahl;
        try {
            ResponseEntity<ReservationDto> result = rt.postForEntity(kautionUrl, null, ReservationDto.class);
            ReservationDto res = result.getBody();
            return res;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Zieht dem Account das reservierte Guthaben ab und überträgt den reservierten Betrag
     * an den zuvor definierten Zielaccount.
     *
     * @param benutzername   Quellaccount
     * @param reservationsId Id der Reservation
     * @return true falls es klappt.
     */
    public boolean kautionEinziehen(String benutzername, int reservationsId) {

        String kautionUrl = url + "reservation/punish/" + benutzername + "?reservationId=" + reservationsId;
        return kautionManager(kautionUrl);
    }

    /**
     * Loese eine Reservierung. In diesem Fall wird dem Account sein Guthaben wieder zur freien Verfügung gegeben
     * und die Reservierung wird restlos geloescht.
     *
     * @param benutzername   Quellaccount
     * @param reservationsId Id der Reservation
     * @return true falls es klappt.
     */
    public boolean kautionFreigeben(String benutzername, int reservationsId) {

        String kautionUrl = url + "reservation/release/" + benutzername + "?reservationId=" + reservationsId;
        return kautionManager(kautionUrl);
    }

    private boolean kautionManager(String kautionUrl) {
        try {
            ResponseEntity<AccountDto> result = rt.postForEntity(kautionUrl, null, AccountDto.class);
            AccountDto acc = result.getBody();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }


}
