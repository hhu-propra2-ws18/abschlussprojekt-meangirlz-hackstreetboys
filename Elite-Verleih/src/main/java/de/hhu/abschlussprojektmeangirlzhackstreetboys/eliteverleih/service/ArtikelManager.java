package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ArtikelManager {

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    ArtikelRepository artikelRepo;

    GeoCoding geoCoder = new GeoCoding();

    public List<Artikel> getAllArtikel() {
        return artikelRepo.findAll();
    }

    /**
     * Erstellt einen Artikel.
     *
     * @param benutzerId Id des Artikels.
     * @param artikel    Artikel.
     */
    public void erstelleArtikel(Long benutzerId, Artikel artikel) {
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        artikel.setBenutzer(benutzer);
        artikel.setArtikelOrtX(geoCoder.getFirstX(artikel.getArtikelOrt()));
        artikel.setArtikelOrtY(geoCoder.getFirstY(artikel.getArtikelOrt()));
        artikel = artikelRepo.save(artikel);
        setzeArtikel(benutzerId, artikel);
    }

    /**
     * Speichert den Artikel bei dem Besitzer ab.
     *
     * @param benutzerId Id des Besitzers.
     * @param artikel    Artikel.
     */
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

    /**
     * Speichert den bearbeiteten Artikel richtig ab.
     *
     * @param artikelId Id des Artikels.
     * @param artikel   Artikel.
     */
    public void bearbeiteArtikel(Long artikelId, Artikel artikel) {
        Artikel alterArtikel = getArtikelById(artikelId);

        alterArtikel.setArtikelBeschreibung(artikel.getArtikelBeschreibung());
        alterArtikel.setArtikelKaution(artikel.getArtikelKaution());
        alterArtikel.setArtikelName(artikel.getArtikelName());
        alterArtikel.setArtikelOrt(artikel.getArtikelOrt());
        alterArtikel.setArtikelOrtX(geoCoder.getFirstX(artikel.getArtikelOrt()));
        alterArtikel.setArtikelOrtY(geoCoder.getFirstY(artikel.getArtikelOrt()));
        alterArtikel.setArtikelTarif(artikel.getArtikelTarif());
        alterArtikel.setArtikelBildUrl(artikel.getArtikelBildUrl());

        artikelRepo.save(alterArtikel);
    }

    /**
     * Loescht den Artikel mit der angegebenen ArtikelId.
     *
     * @param artikelId Die ID des Artikels.
     */
    public void loescheArtikel(Long artikelId) {
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
