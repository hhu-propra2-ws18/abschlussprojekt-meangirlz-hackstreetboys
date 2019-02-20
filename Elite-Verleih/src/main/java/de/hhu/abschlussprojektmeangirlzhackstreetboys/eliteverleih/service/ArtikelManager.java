package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
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
public class ArtikelManager {

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    ArtikelRepository artikelRepo;

    public List<Artikel> getAllArtikel() {
        return artikelRepo.findAll();
    }

    public void erstelleArtikel(Long benutzerId, Artikel artikel) {
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        artikel.setBenutzer(benutzer);
        artikel = artikelRepo.save(artikel);
        setzeArtikel(benutzerId, artikel);
    }

    public void setzeArtikel(Long benutzerId, Artikel artikel) {
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        if (b.getArtikel() == null) {
            b.setArtikel(new ArrayList<Artikel>());
        }
        b.getArtikel().add(artikel);
        benutzerRepo.save(b);
    }

    public Artikel getArtikelById(Long artikelId) {
        return artikelRepo.findArtikelByArtikelId(artikelId);
    }

    void bearbeiteArtikel(Long artikelId, Artikel artikel) {
        Artikel alterArtikel = getArtikelById(artikelId);

        alterArtikel.setArtikelBeschreibung(artikel.getArtikelBeschreibung());
        alterArtikel.setArtikelKaution(artikel.getArtikelKaution());
        alterArtikel.setArtikelName(artikel.getArtikelName());
        alterArtikel.setArtikelOrt(artikel.getArtikelOrt());
        alterArtikel.setArtikelTarif(artikel.getArtikelTarif());

        artikelRepo.saveAll(Arrays.asList(alterArtikel));
    }

    public void deleteArtikel(Long artikelId) {
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerId(artikelRepo.findArtikelByArtikelId(artikelId)
            .getBenutzer()
            .getBenutzerId());

        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        List<Ausleihe> ausleihen = benutzer.getAusgeliehen();
        if (artikel.getAusgeliehen().isEmpty()) {
            benutzer.getArtikel().remove(artikel);
            benutzerRepo.save(benutzer);
            artikelRepo.delete(artikel);
        } else {
            for (Ausleihe a : ausleihen) {
                if (a.getAusleihStatus() != Status.BESTAETIGT) {
                    benutzer.getArtikel().remove(artikel);
                    benutzerRepo.save(benutzer);
                    artikelRepo.delete(artikel);
                }
            }
        }
    }

    public List<Artikel> getArtikelListSortByName(String suchBegriff) {
        return artikelRepo.findAllByArtikelNameContainsOrderByArtikelNameAsc(suchBegriff);
    }
}
