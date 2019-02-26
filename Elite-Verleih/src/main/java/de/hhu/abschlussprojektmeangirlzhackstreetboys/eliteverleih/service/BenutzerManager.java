package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Benutzer erstelleBenutzer(Benutzer benutzer) {
        if (nameSchonVorhanden(benutzer.getBenutzerName())) {
            return null;
        }
        AccountDto account = propayManager.getAccount(benutzer.getBenutzerName());
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


    public void geldAufladen(Benutzer newBenutzer, int aufladen) {
        propayManager.guthabenAufladen(newBenutzer.getBenutzerName(), aufladen);
    }
}



