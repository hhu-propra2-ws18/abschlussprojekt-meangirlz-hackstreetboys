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
import java.util.List;

@Service
public class ArtikelManager {

    final BenutzerRepository benutzerRepo;
    final ArtikelRepository artikelRepo;
    GeoCoding geoCoder = new GeoCoding();

    @Autowired
    public ArtikelManager(BenutzerRepository benutzerRepo, ArtikelRepository artikelRepo) {
        this.benutzerRepo = benutzerRepo;
        this.artikelRepo = artikelRepo;
    }

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
        setzteArtikelOrtAttribute(artikel, artikel.getArtikelOrt());
        artikel = artikelRepo.save(artikel);
        setzeArtikel(benutzerId, artikel);
    }

    /**
     * Erstellt einen Artikel, der verkauft werden kann.
     *
     * @param benutzerId Id des Artikels.
     * @param artikel    Artikel.
     */
    public void erstelleVerkauf(Long benutzerId, Artikel artikel) {
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        artikel.setBenutzer(benutzer);
        artikel.setArtikelKaution(0);
        artikel.setArtikelTarif(0);
        artikel.setZuVerkaufen(true);
        setzteArtikelOrtAttribute(artikel, artikel.getArtikelOrt());
        artikel = artikelRepo.save(artikel);
        setzeArtikel(benutzerId, artikel);
    }

    /**
     * Speichert den Artikel bei dem Besitzer ab.
     *
     * @param benutzerId Id des Besitzers.
     * @param artikel    Artikel.
     */
    private void setzeArtikel(Long benutzerId, Artikel artikel) {
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
        setzteArtikelOrtAttribute(alterArtikel, artikel.getArtikelOrt());
        alterArtikel.setArtikelTarif(artikel.getArtikelTarif());
        alterArtikel.setArtikelBildUrl(artikel.getArtikelBildUrl());
        alterArtikel.setArtikelPreis(artikel.getArtikelPreis());

        artikelRepo.save(alterArtikel);
    }

    /**
     * Setzt den artikelOrt sowie die Koordinaten artikelOrtX und artikelOrtY.
     *
     * @param zuSetzenderArtikel Artikel dessen Attribute gesetzt werden
     * @param artikelOrt artikelOrt String mit Adresse
     */
    public void setzteArtikelOrtAttribute(Artikel zuSetzenderArtikel, String artikelOrt) {
        String artikelOrtOhneUmlaute = ersetzeUmlaute(artikelOrt);
        zuSetzenderArtikel.setArtikelOrt(artikelOrt);
        zuSetzenderArtikel.setArtikelOrtX(geoCoder.erhalteErstesX(artikelOrtOhneUmlaute));
        zuSetzenderArtikel.setArtikelOrtY(geoCoder.erhalteErstesY(artikelOrtOhneUmlaute));
    }

    /**
     * wandelt deutsche Umlaute in englische gleichgestellte bustaben um.
     *
     * @param verdeutschterString String mit Umlauten
     * @return verenglischterString ohne Umlaute
     */
    public String ersetzeUmlaute(String verdeutschterString) {
        String[][] umlauteUndErsetzungen = {{"Ä", "Ae"}, {"Ü", "Ue"}, {"Ö", "Oe"}, {"ä", "ae"}, {"ü", "ue"},
            {"ö", "oe"}, {"ß", "ss"}};
        String verEnglischt = verdeutschterString;
        for (int i = 0; i < umlauteUndErsetzungen.length; i = i + 1) {
            verEnglischt = verEnglischt.replaceAll(umlauteUndErsetzungen[i][0], umlauteUndErsetzungen[i][1]);
        }
        return verEnglischt;
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
        if (!istAusgeliehen(artikelId)) {
            benutzer.getArtikel().remove(artikel);
            benutzerRepo.save(benutzer);
            artikelRepo.delete(artikel);
        }
    }

    private boolean istAusgeliehen(Long artikelId) {
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        if (artikel.getAusgeliehen() == null || artikel.getAusgeliehen().isEmpty()) {
            return false;
        }
        for (Ausleihe a : artikel.getAusgeliehen()) {
            Status status = a.getAusleihStatus();
            if (status == Status.BESTAETIGT || status == Status.AKTIV || status == Status.KONFLIKT) {
                return true;
            }
        }
        return false;
    }

    public List<Artikel> getArtikelListSortByName(String suchBegriff) {
        return artikelRepo.findAllByArtikelNameContainsOrderByArtikelNameAsc(suchBegriff);
    }
}
