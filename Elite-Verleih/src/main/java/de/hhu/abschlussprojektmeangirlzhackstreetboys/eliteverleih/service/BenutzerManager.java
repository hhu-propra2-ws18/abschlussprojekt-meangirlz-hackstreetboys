package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BenutzerManager {


    final BenutzerRepository benutzerRepo;
    PropayManager propayManager;

    @Autowired
    public BenutzerManager(BenutzerRepository benutzerRepo, PropayManager propayManager){
        this.benutzerRepo = benutzerRepo;
        this.propayManager = propayManager;
    }

    public List<Benutzer> getAllBenutzer() {
        return benutzerRepo.findAll();
    }

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
     * Erstellt einen Benutzer und gibt ihn zurueck
     * @param benutzer
     * @return Benutzer mit Id -1 wenn es schon einen in der Datenbank gibt.
     * , null wenn Propay nicht laeuft.
     */
    public Benutzer erstelleBenutzer(Benutzer benutzer) {
        if (nameSchonVorhanden(benutzer.getBenutzerName())) {
            Benutzer benutzerFehler = new Benutzer();
            benutzerFehler.setBenutzerId(-1L);
            return benutzerFehler;
        }
        AccountDto account = propayManager.getAccount(benutzer.getBenutzerName());
        if (account == null){
            return null;
        }
        return benutzerRepo.save(benutzer);
    }

    public Benutzer findBenutzerByName(String name) {

        if (!benutzerRepo.findBenutzerByBenutzerName(name).isPresent()) {
            return null;
        }

        return benutzerRepo.findBenutzerByBenutzerName(name).get();
    }

    public void bearbeiteBenutzer(Long benutzerId, Benutzer benutzer) {
        Benutzer alterBenutzer = getBenutzerById(benutzerId);

        alterBenutzer.setBenutzerEmail(benutzer.getBenutzerEmail());

        benutzerRepo.save(alterBenutzer);
    }

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
        if (code != 200){
            return false;
        }
        return true;
    }
}



