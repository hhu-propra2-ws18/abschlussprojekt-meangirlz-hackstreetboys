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

    final TransaktionRepository transaktionRepo;
    final ArtikelRepository artikelRepo;
    final AusleiheRepository ausleiheRepo;
    final BenutzerRepository benutzerRepo;
    PropayManager propayManager;

    @Autowired
    public TransaktionManager(TransaktionRepository transaktionRepo,
                              ArtikelRepository artikelRepo,
                              AusleiheRepository ausleiheRepo,
                              BenutzerRepository benutzerRepo,
                              PropayManager propayManager){
        this.transaktionRepo = transaktionRepo;
        this.artikelRepo = artikelRepo;
        this.ausleiheRepo = ausleiheRepo;
        this.benutzerRepo = benutzerRepo;
        this.propayManager = propayManager;
    }

    /**
     * Getter fuer Transaktionen
     *
     * @return Liste von Transaktionen
     */
    public List<Transaktion> getAllTransaktion(){
        return transaktionRepo.findAll();
    }

    /**
     * Erstellt eine Transaktion mit allen Abhaengigkeiten
     *
     * @param ausleiheId        Id der Ausleihe
     *
     * //@param transaktionBetrag Betrag, welcher verbucht wird
     * @return Transaktion.
     */

    public Transaktion erstelleTransaktion(Long ausleiheId){
        Transaktion transaktion = new Transaktion();
        Ausleihe ausleihe = ausleiheRepo.findAusleiheByAusleihId(ausleiheId);

        transaktion.setAusleihe(ausleihe);
        transaktion.setTransaktionBetrag(setzeTransaktionBetrag(ausleiheId)); // Setze gesamtbetrag

        System.out.println("TEST");
        System.out.println(transaktion.getTransaktionBetrag());

        setzeTransaktionBenutzer(ausleihe.getBenutzer().getBenutzerId(), transaktion);

        return transaktion;
    }

    private void setzeTransaktionBenutzer(Long benutzerId, Transaktion transaktion){
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        if(b.getTransaktionen() == null ){
            b.setTransaktionen(new ArrayList<Transaktion>());
        }
        b.getTransaktionen().add(transaktion);
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
