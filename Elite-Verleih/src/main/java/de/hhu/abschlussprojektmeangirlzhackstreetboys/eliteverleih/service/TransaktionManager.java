package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.AusleiheRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.TransaktionRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class TransaktionManager {

    @Autowired
    TransaktionRepository transaktionRepo;
    @Autowired
    ArtikelRepository artikelRepo;
    @Autowired
    AusleiheRepository ausleiheRepo;
    @Autowired
    BenutzerRepository benutzerRepo;

    PropayManager propayManager;

    @Autowired
    public TransaktionManager(){}

    /**
     * Getter fuer Transaktionen
     *
     * @return Liste von Transaktionen
     */
    public List<Transaktion> getAllTransaktion(String benutzerName){
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerName(benutzerName).get();
        return benutzer.getTransaktionen();

    }

    /**
     * Erstellt eine Transaktion mit allen Abhaengigkeiten
     *
     * @param ausleiheId        Id der Ausleihe
     * @return Transaktion.
     */

    public Transaktion erstelleTransaktion(Long ausleiheId){
        Transaktion transaktion = new Transaktion();
        Ausleihe ausleihe = ausleiheRepo.findAusleiheByAusleihId(ausleiheId);

        transaktion.setArtikelName(ausleihe.getArtikel().getArtikelName());
        transaktion.setVerleihenderName(ausleihe.getArtikel().getBenutzer().getBenutzerName());
        transaktion.setAusleihenderName(ausleihe.getBenutzer().getBenutzerName());

        transaktion.setTransaktionBetrag(setzeTransaktionBetrag(ausleiheId));
        setzeTransaktionBenutzer(ausleihe.getBenutzer().getBenutzerId(), transaktion);
        setzeTransaktionBenutzer(ausleihe.getArtikel().getBenutzer().getBenutzerId(), transaktion);

        return transaktion;
    }

    public Transaktion erstelleTransaktionKaution(Long ausleiheId){
        Transaktion transaktion = new Transaktion();
        Ausleihe ausleihe = ausleiheRepo.findAusleiheByAusleihId(ausleiheId);

        transaktion.setArtikelName(ausleihe.getArtikel().getArtikelName());
        transaktion.setVerleihenderName(ausleihe.getArtikel().getBenutzer().getBenutzerName());
        transaktion.setAusleihenderName(ausleihe.getBenutzer().getBenutzerName());

        System.out.println(ausleihe.getArtikel().getArtikelKaution());
        transaktion.setTransaktionBetrag(ausleihe.getArtikel().getArtikelKaution());
        setzeTransaktionBenutzer(ausleihe.getBenutzer().getBenutzerId(), transaktion);
        setzeTransaktionBenutzer(ausleihe.getArtikel().getBenutzer().getBenutzerId(), transaktion);

        return transaktion;
    }

    public Transaktion erstelleTransaktionVerkauf(Long artikelId, String benutzerName){
        Transaktion transaktion = new Transaktion();
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerName(benutzerName).get();

        transaktion.setArtikelName(artikel.getArtikelName());
        transaktion.setVerleihenderName(artikel.getBenutzer().getBenutzerName());
        transaktion.setAusleihenderName(benutzerName);

        transaktion.setTransaktionBetrag(artikel.getArtikelPreis());
        setzeTransaktionBenutzer(artikel.getBenutzer().getBenutzerId(), transaktion);
        setzeTransaktionBenutzer(benutzer.getBenutzerId(), transaktion);

        return transaktion;
    }

    private void setzeTransaktionBenutzer(Long benutzerId, Transaktion transaktion){
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        if(b.getTransaktionen() == null ){
            b.setTransaktionen(new ArrayList<Transaktion>());
        }
        b.getTransaktionen().add(transaktion);
        transaktionRepo.save(transaktion);
        benutzerRepo.save(b);
    }

    public int setzeTransaktionBetrag(Long ausleiheId){
        Ausleihe ausleihe = ausleiheRepo.findAusleiheByAusleihId(ausleiheId);
        if(ausleihe.getAusleihStatus() == Status.ABGEGEBEN ){
            return ausleihe.berechneKosten(getHeutigesDatum());
        } return 0;
    }

    private Calendar getHeutigesDatum(){
        Calendar heute = new GregorianCalendar();
        return heute;
    }

}