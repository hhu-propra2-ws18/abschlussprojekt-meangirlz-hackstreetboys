package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.AusleiheRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AusleiheManager {

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    ArtikelRepository artikelRepo;

    @Autowired
    AusleiheRepository ausleiheRepo;

    public List<Ausleihe> getAllAusleihe() {
        return ausleiheRepo.findAll();
    }

    public Ausleihe erstelleAusleihe(Long benutzerId, Long artikelId, Date ausleihStartdatum, Date ausleihRueckgabedatum){
        Ausleihe ausleihe = new Ausleihe();
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        ausleihe.setBenutzer(benutzer);
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        ausleihe.setArtikel(artikel);
        ausleihe.setAusleihRueckgabedatum(ausleihRueckgabedatum);
        ausleihe.setAusleihStartdatum(ausleihStartdatum);
        ausleihe.setAusleihStatus(Status.ANGEFRAGT);
        ausleihe = ausleiheRepo.save(ausleihe);
        setzeAusleiheBenutzer(benutzerId,ausleihe);
        setzeAusleiheArtikel(artikelId,ausleihe);
        return ausleihe;
    }

    private void setzeAusleiheBenutzer(Long benutzerId, Ausleihe ausleihe){
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        if(b.getAusgeliehen()==null) b.setAusgeliehen(new ArrayList<Ausleihe>());
        b.getAusgeliehen().add(ausleihe);
        benutzerRepo.save(b);
    }

    private void setzeAusleiheArtikel(Long artikelId, Ausleihe ausleihe){
        Artikel a = artikelRepo.findArtikelByArtikelId(artikelId);
        if(a.getAusgeliehen()==null) a.setAusgeliehen(new ArrayList<Ausleihe>());
        a.getAusgeliehen().add(ausleihe);
        artikelRepo.save(a);
    }

    public Ausleihe getAusleiheById(Long ausleiheId){
        return ausleiheRepo.findAusleiheByAusleihId(ausleiheId);
    }

    public void bestaetigeAusleihe(Ausleihe ausleihe){
        ausleihe.setAusleihStatus(Status.BESTAETIGT);
        ausleiheRepo.save(ausleihe);
    }

    public void loescheAusleihe(Long ausleihId){
        Ausleihe a = ausleiheRepo.findAusleiheByAusleihId(ausleihId);
        loescheAusleiheFuerBenutzer(a.getBenutzer().getBenutzerId(),a);
        loescheAusleiheFuerArtikelundBesitzer(a.getArtikel().getBenutzer().getBenutzerId(),a.getArtikel(),a);
        ausleiheRepo.delete(a);
    }

    private void loescheAusleiheFuerBenutzer(Long benutzerId, Ausleihe ausleihe){//muss mit Ausleihe aus dem Repo augerufen werden
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        b.getAusgeliehen().remove(ausleihe);
        benutzerRepo.save(b);
    }

    private void loescheAusleiheFuerArtikelundBesitzer(Long benutzerId, Artikel artikel, Ausleihe ausleihe){
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        List<Artikel> alArt = b.getArtikel();
        for(Artikel a : alArt){
            if(a.getArtikelId()==artikel.getArtikelId()){
                int i = alArt.indexOf(a);
                System.err.println("i: "+i);
                System.err.println("a.getAusgeliehen.size(): "+a.getAusgeliehen().size());
                a.getAusgeliehen().remove(ausleihe);
                artikelRepo.save(a);
                System.err.println("a.getAusgeliehen.size(): "+a.getAusgeliehen().size());
                alArt.set(i,a);
            }
        }
        b.setArtikel(alArt);
        benutzerRepo.save(b);
    }

    public Ausleihe bearbeiteAusleihe(Long ausleiheId, Status neuerAusleiheStatus){
        Ausleihe newA = getAusleiheById(ausleiheId);
        newA.setAusleihStatus(neuerAusleiheStatus);
        return ausleiheRepo.save(newA);
    }
}
