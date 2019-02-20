package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller.DataSync;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.AusleiheRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dto.ReservationDTO;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AusleiheManager {

	DataSync sync = new DataSync();

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

    public void bestaetigeAusleihe(Long ausleiheId){
        bearbeiteAusleihe(ausleiheId, Status.BESTAETIGT);
        loescheKollidierendeAnfragen(ausleiheId);
        Ausleihe ausleihe = getAusleiheById(ausleiheId);
        Artikel artikel = ausleihe.getArtikel();
        ReservationDTO r1 = sync.kautionReserviern(ausleihe.getBenutzer().getBenutzerName(),
                artikel.getBenutzer().getBenutzerName(), artikel.getArtikelKaution());
        ausleihe.setReservationsId(r1.getId());
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

    private void loescheKollidierendeAnfragen(Long ausleiheId) {
        Artikel artikel = getAusleiheById(ausleiheId).getArtikel();
        List<Ausleihe> ausleihList = artikel.getAusgeliehen();
        for(Ausleihe a : ausleihList) {
            if(a.getAusleihId() != ausleiheId) {
                if(kollidiertMitAusleihe(a.getAusleihId(), ausleiheId)) {
                    bearbeiteAusleihe(a.getAusleihId(),Status.ABGELEHNT);
                }
            }
        }
        for(Ausleihe a: ausleihList) {
            if(a.getAusleihStatus().equals(Status.ABGELEHNT)) {
                loescheAusleihe(ausleiheId);
            }
        }
    }

    private boolean kollidiertMitAusleihe(Long aId, Long akzeptierteAId) {
        Ausleihe ausleihe = getAusleiheById(aId);
        Ausleihe akzeptierteAusleihe = getAusleiheById(akzeptierteAId);
        Date endDatum = akzeptierteAusleihe.getAusleihRueckgabedatum();
        Date startDatum = akzeptierteAusleihe.getAusleihStartdatum();
        if(ausleihe.getAusleihStartdatum().before(endDatum) && ausleihe.getAusleihStartdatum().after(startDatum)){
            return true;
        }
        if(ausleihe.getAusleihRueckgabedatum().after(startDatum) && ausleihe.getAusleihRueckgabedatum().before(endDatum)) {
            return true;
        }
        if(ausleihe.getAusleihStartdatum().equals(startDatum) || ausleihe.getAusleihStartdatum().equals(endDatum)) {
            return true;
        }
        if(ausleihe.getAusleihRueckgabedatum().equals(startDatum)|| ausleihe.getAusleihRueckgabedatum().equals(endDatum)) {
            return true;
        }
        return false;
    }

    public boolean isAusgeliehen (Long artikelId, Date startDatum, Date endDatum) {
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        for(Ausleihe ausleihe : artikel.getAusgeliehen()) {
            if(!endDatum.before(ausleihe.getAusleihStartdatum()) && !(startDatum.after(ausleihe.getAusleihRueckgabedatum()))) {
                return false;
            }
        }
        return true;
    }

    public void lehneAusleiheAb(Long ausleiheId) {
        bearbeiteAusleihe(ausleiheId,Status.ABGELEHNT);
    }

    public void zurueckGeben(Long ausleiheId) {
        Ausleihe ausleihe = getAusleiheById(ausleiheId);
        int tage = getAnzahlTage(ausleiheId);
        int kosten = ausleihe.getArtikel().getArtikelTarif() * tage;
        sync.ueberweisen(ausleihe.getBenutzer().getBenutzerName(), ausleihe.getArtikel().getBenutzer().getBenutzerName(), kosten);
        //System.out.println(tage);
        bearbeiteAusleihe(ausleiheId,Status.ABGEGEBEN);
    }

    private int getAnzahlTage(Long ausleiheId) {
        int ergebnis = 0;
        Date start = getAusleiheById(ausleiheId).getAusleihStartdatum();
        Date date=new Date(System.currentTimeMillis());
        if(date.equals(start)){
            return 1;
        }
        if(date.after(start)){
            long milli = date.getTime() - start.getTime();
            ergebnis = (int) TimeUnit.DAYS.convert(milli, TimeUnit.MILLISECONDS) + 1;
        }
        return ergebnis;
    }

    public void rueckgabeAkzeptieren(Long ausleiheId) {
        bearbeiteAusleihe(ausleiheId,Status.BEENDET);
    }

    public List<Ausleihe> getKonflike(List<Ausleihe> liste){
        List<Ausleihe> konflikeAusleihe = new ArrayList<>();
        for( Ausleihe a: liste ) {
            if (a.getAusleihStatus()== Status.KONFLIKT){
                konflikeAusleihe.add(a);
            }
        }
        return konflikeAusleihe;
    }

    public void setzeSatusAusleihe(Long ausleiheId, String name){
        bearbeiteAusleihe(ausleiheId,Status.valueOf(name));
    }

    public void konfliktAusleihe(Long ausleihId) {
        bearbeiteAusleihe(ausleihId,Status.KONFLIKT);
    }
}
