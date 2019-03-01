package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BenutzerManager {


    final BenutzerRepository benutzerRepo;
    PropayManager propayManager;

    @Autowired
    public BenutzerManager(BenutzerRepository benutzerRepo, PropayManager propayManager) {
        this.benutzerRepo = benutzerRepo;
        this.propayManager = propayManager;
    }

    public List<Benutzer> getAllBenutzer() {
        return benutzerRepo.findAll();
    }


    /**
     * Prueft ob ein Benutzername bereits vorhanden ist.
     *
     * @param name zu untersuchender Name
     * @return true falls Name schon existiert, sonst false
     */
    public boolean nameSchonVorhanden(String name) {
        List<Benutzer> alleBenutzer = getAllBenutzer();

        for (Benutzer benutzer : alleBenutzer) {
            if (benutzer.getBenutzerName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Benutzer getBenutzerById(Long benutzerId) {
        return benutzerRepo.findBenutzerByBenutzerId(benutzerId);
    }

    /**
     * Erstellt einen Benutzer und gibt ihn zurueck.
     *
     * @param benutzer Benutzer der angelegt werden soll
     * @return Benutzer mit Id -1 wenn es schon einen in der Datenbank gibt.
     *     , null wenn Propay nicht laeuft.
     */
    public Benutzer erstelleBenutzer(Benutzer benutzer) {
        if (nameSchonVorhanden(benutzer.getBenutzerName())) {
            Benutzer benutzerFehler = new Benutzer();
            benutzerFehler.setBenutzerId(-1L);
            return benutzerFehler;
        }
        AccountDto account = propayManager.getAccount(benutzer.getBenutzerName());
        if (account == null) {
            return null;
        }
        return benutzerRepo.save(benutzer);
    }

    /**
     * Suche Benutzer mittels Name.
     *
     * @param name nach diesem Namen soll gesucht werden
     * @return null, falls BEnutzer nicht vorhanden, sonst Benutzer
     */
    public Benutzer findBenutzerByName(String name) {

        if (!benutzerRepo.findBenutzerByBenutzerName(name).isPresent()) {
            return null;
        }

        return benutzerRepo.findBenutzerByBenutzerName(name).get();
    }

    /**
     * Benutzerwerte neu setzen.
     *
     * @param benutzerId Id des Benutzers
     * @param benutzer Benutzer mit neuen Werten
     */

    public void bearbeiteBenutzer(Long benutzerId, Benutzer benutzer) {
        Benutzer alterBenutzer = getBenutzerById(benutzerId);

        alterBenutzer.setBenutzerEmail(benutzer.getBenutzerEmail());

        benutzerRepo.save(alterBenutzer);
    }

    /**
     * Anfragen für diesen Benutzer finden.
     *
     * @param benutzer Anfragen dieses Benutzers
     * @param status Status, der Anfragen
     * @return Liste der Anfragen für diesen Benutzer mit angegebenem Status
     */
    public List<Ausleihe> sucheEingehendeAnfragen(Benutzer benutzer, Status status) {
        List<Ausleihe> wartend = new ArrayList<>();
        for (Artikel a : benutzer.getArtikel()) {
            for (Ausleihe b : a.getAusgeliehen()) {
                if (b.getAusleihStatus().equals(status)) {
                    wartend.add(b);
                }
            }
        }
        return wartend;
    }

    /**
     * Anfragen von diesem Benutzer finden.
     *
     * @param benutzer Anfragen dieses Benutzers
     * @param status Status, der Anfragen
     * @return Liste der Anfragen von diesem Benutzer mit angegebenem Status
     */
    public List<Ausleihe> sucheAusgehendeAnfragen(Benutzer benutzer, Status status) {
        List<Ausleihe> list = new ArrayList<Ausleihe>();
        for (Ausleihe b : benutzer.getAusgeliehen()) {
            if (b.getAusleihStatus().equals(status)) {
                list.add(b);
            }
        }
        return list;
    }


    public boolean geldAufladen(Benutzer newBenutzer, int aufladen) {
        int code = propayManager.guthabenAufladen(newBenutzer.getBenutzerName(), aufladen);
        return code == 200;
    }

    /**
     * Aus Benutzer werden verspaetete Ausleihen gesucht.
     *
     * @param benutzer Eingeloggte Benutzer.
     * @return Liste mit verspaeteten Ausleihen.
     */
    public List<Ausleihe> findeVerspaeteteAusleihe(Benutzer benutzer) {

        Calendar heute = new GregorianCalendar();
        List<Ausleihe> verspaeteAusleihe = new ArrayList<>();

        for (Ausleihe a: benutzer.getAusgeliehen()) {
            if ((a.getAusleihStatus() == Status.BESTAETIGT) && (heute.after(a.getAusleihRueckgabedatum()))) {
                verspaeteAusleihe.add(a);
            }
        }

        return verspaeteAusleihe;
    }
}



