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

    /**
     * Erstellt einen Artikel, der ausgeliehen werden kann.
     *
     * @param benutzerId Id des Artikels.
     * @param artikel    Artikel.
     */
    public void erstelleVerleihen(Long benutzerId, Artikel artikel) {
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        artikel.setBenutzer(benutzer);
        artikel.setArtikelPreis(0);
        artikel.setZuVerkaufen(false);
        artikel = artikelRepo.save(artikel);
        setzeArtikel(benutzerId, artikel);
    }

    /**
     * Erstellt einen Artikel, der verkauft werden kann.
     *
     * @param benutzerId Id des Artikels.
     * @param artikel Artikel.
     */
    public void erstelleVerkauf(Long benutzerId, Artikel artikel) {
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        artikel.setBenutzer(benutzer);
        artikel.setArtikelKaution(0);
        artikel.setArtikelTarif(0);
        artikel.setZuVerkaufen(true);
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
        alterArtikel.setArtikelTarif(artikel.getArtikelTarif());
        alterArtikel.setArtikelBildUrl(artikel.getArtikelBildUrl());
        alterArtikel.setArtikelPreis(artikel.getArtikelPreis());

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
