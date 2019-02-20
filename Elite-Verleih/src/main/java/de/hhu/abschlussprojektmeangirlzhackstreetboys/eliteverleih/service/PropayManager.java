package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDTO;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.ReservationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PropayManager {

    RestTemplate rt = new RestTemplate();
    final String URL = "http://localhost:8888/";

    public AccountDTO getAccount(String benutzername){

        ResponseEntity<AccountDTO> result = rt.getForEntity(URL + "account/"+ benutzername , AccountDTO.class);
        AccountDTO acc = result.getBody();
        return acc;
    }

    public void GuthabenAufladen(String benutzername, int anzahl){

        ResponseEntity<AccountDTO> result = rt.postForEntity(URL + "account/"+ benutzername +"?amount="+anzahl,null, AccountDTO.class);
        AccountDTO acc = result.getBody();

    }
    public boolean ueberweisen(String vonBenutzername, String zuBenutzername, int anzahl){

        String urlueberweisenUrl = URL + "account/"+ vonBenutzername +"/transfer/"+ zuBenutzername +"?amount=" + anzahl;
        try {
            rt.postForEntity(urlueberweisenUrl,null,AccountDTO.class);
            return true;
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    public ReservationDTO kautionReserviern(String vonBenutzername, String zuBenutzername, int anzahl){

        String kautionUrl = URL + "reservation/reserve/"+ vonBenutzername +"/"+zuBenutzername+"?amount=" + anzahl;
        try {
            ResponseEntity<ReservationDTO> result = rt.postForEntity(kautionUrl, null, ReservationDTO.class);
            ReservationDTO res = result.getBody();
            return res;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean kautionEinziehen(String benutzername, int reservationsid){

        String kautionUrl = URL + "reservation/punish/"+ benutzername +"?reservationId=" + reservationsid;
        return kautionManager(kautionUrl);
    }

    public boolean kautionFreigeben(String benutzername, int reservationsid){

        String kautionUrl = URL + "reservation/release/"+ benutzername +"?reservationId=" + reservationsid;
        return kautionManager(kautionUrl);
    }

    private boolean kautionManager(String kautionUrl) {
        try {
            ResponseEntity<AccountDTO> result = rt.postForEntity(kautionUrl, null, AccountDTO.class);
            AccountDTO acc = result.getBody();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
