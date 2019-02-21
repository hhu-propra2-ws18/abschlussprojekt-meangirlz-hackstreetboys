package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.AusleiheRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.ReservationDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AusleiheManager {

    PropayManager propayManager = new PropayManager();

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    ArtikelRepository artikelRepo;

    @Autowired
    AusleiheRepository ausleiheRepo;

    /**
     *
     * @return
     */
    public List<Ausleihe> getAllAusleihe() {
        return ausleiheRepo.findAll();
    }

    /**
     *
     * @param benutzerId
     * @param artikelId
     * @param ausleihStartdatum
     * @param ausleihRueckgabedatum
     * @return
     */
    public Ausleihe erstelleAusleihe(Long benutzerId,
                                     Long artikelId,
                                     Calendar ausleihStartdatum,
                                     Calendar ausleihRueckgabedatum) {
        Ausleihe ausleihe = new Ausleihe();
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        ausleihe.setBenutzer(benutzer);
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        ausleihe.setArtikel(artikel);
        ausleihe.setAusleihRueckgabedatum(ausleihRueckgabedatum);
        ausleihe.setAusleihStartdatum(ausleihStartdatum);
        ausleihe.setAusleihStatus(Status.ANGEFRAGT);
        ausleihe = ausleiheRepo.save(ausleihe);
        setzeAusleiheBenutzer(benutzerId, ausleihe);
        setzeAusleiheArtikel(artikelId, ausleihe);
        return ausleihe;
    }

    /**
     *
     * @param benutzerId
     * @param ausleihe
     */
    private void setzeAusleiheBenutzer(Long benutzerId, Ausleihe ausleihe) {
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        if (b.getAusgeliehen() == null) {
            b.setAusgeliehen(new ArrayList<Ausleihe>());
        }
        b.getAusgeliehen().add(ausleihe);
        benutzerRepo.save(b);
    }

    /**
     *
     * @param artikelId
     * @param ausleihe
     */
    private void setzeAusleiheArtikel(Long artikelId, Ausleihe ausleihe) {
        Artikel a = artikelRepo.findArtikelByArtikelId(artikelId);
        if (a.getAusgeliehen() == null) {
            a.setAusgeliehen(new ArrayList<Ausleihe>());
        }
        a.getAusgeliehen().add(ausleihe);
        artikelRepo.save(a);
    }

    /**
     *
     * @param ausleiheId
     * @return
     */
    public Ausleihe getAusleiheById(Long ausleiheId) {
        return ausleiheRepo.findAusleiheByAusleihId(ausleiheId);
    }

    /**
     *
     * @param ausleiheId
     */
    public void bestaetigeAusleihe(Long ausleiheId) {
        bearbeiteAusleihe(ausleiheId, Status.BESTAETIGT);
        loescheKollidierendeAnfragen(ausleiheId);
        Ausleihe ausleihe = getAusleiheById(ausleiheId);
        Artikel artikel = ausleihe.getArtikel();
        ReservationDto r1 = propayManager.kautionReserviern(ausleihe.getBenutzer().getBenutzerName(),
            artikel.getBenutzer().getBenutzerName(), artikel.getArtikelKaution());
        ausleihe.setReservationsId(r1.getId());
        ausleiheRepo.save(ausleihe);
    }

    /**
     *
     * @param ausleihId
     */
    public void loescheAusleihe(Long ausleihId) {
        Ausleihe a = ausleiheRepo.findAusleiheByAusleihId(ausleihId);
        loescheAusleiheFuerBenutzer(a.getBenutzer().getBenutzerId(), a);
        loescheAusleiheFuerArtikelundBesitzer(a.getArtikel().getBenutzer().getBenutzerId(), a.getArtikel(), a);
        ausleiheRepo.delete(a);
    }

    /**
     *
     * @param benutzerId
     * @param ausleihe
     */
    private void loescheAusleiheFuerBenutzer(Long benutzerId, Ausleihe ausleihe) { //muss mit Ausleihe aus dem Repo augerufen werden
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        b.getAusgeliehen().remove(ausleihe);
        benutzerRepo.save(b);
    }

    /**
     *
     * @param benutzerId
     * @param artikel
     * @param ausleihe
     */
    private void loescheAusleiheFuerArtikelundBesitzer(Long benutzerId, Artikel artikel, Ausleihe ausleihe) {
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        List<Artikel> alArt = b.getArtikel();
        for (Artikel a : alArt) {
            if (a.getArtikelId() == artikel.getArtikelId()) {
                int i = alArt.indexOf(a);
                System.err.println("i: " + i);
                System.err.println("a.getAusgeliehen.size(): " + a.getAusgeliehen().size());
                a.getAusgeliehen().remove(ausleihe);
                artikelRepo.save(a);
                System.err.println("a.getAusgeliehen.size(): " + a.getAusgeliehen().size());
                alArt.set(i, a);
            }
        }
        b.setArtikel(alArt);
        benutzerRepo.save(b);
    }

    /**
     *
     * @param ausleiheId
     * @param neuerAusleiheStatus
     * @return
     */
    public Ausleihe bearbeiteAusleihe(Long ausleiheId, Status neuerAusleiheStatus) {
        Ausleihe newA = getAusleiheById(ausleiheId);
        newA.setAusleihStatus(neuerAusleiheStatus);
        return ausleiheRepo.save(newA);
    }

    /**
     *
     * @param ausleiheId
     */
    private void loescheKollidierendeAnfragen(Long ausleiheId) {
        Artikel artikel = getAusleiheById(ausleiheId).getArtikel();
        List<Ausleihe> ausleihList = artikel.getAusgeliehen();
        for (Ausleihe a : ausleihList) {
            if (a.getAusleihId() != ausleiheId) {
                if (kollidiertMitAusleihe(a.getAusleihId(), ausleiheId)) {
                    bearbeiteAusleihe(a.getAusleihId(), Status.ABGELEHNT);
                }
            }
        }
        for (Ausleihe a : ausleihList) {
            if (a.getAusleihStatus().equals(Status.ABGELEHNT)) {
                loescheAusleihe(ausleiheId);
            }
        }
    }

    /**
     *
     * @param ausleiheId
     * @param akzeptierteAId
     * @return
     */
    private boolean kollidiertMitAusleihe(Long ausleiheId, Long akzeptierteAId) {
        Ausleihe ausleihe = getAusleiheById(ausleiheId);
        Ausleihe akzeptierteAusleihe = getAusleiheById(akzeptierteAId);
        Calendar endDatum = akzeptierteAusleihe.getAusleihRueckgabedatum();
        Calendar startDatum = akzeptierteAusleihe.getAusleihStartdatum();
        if (ausleihe.getAusleihStartdatum().before(endDatum)
            && ausleihe.getAusleihStartdatum().after(startDatum)) {
            return true;
        }
        if (ausleihe.getAusleihRueckgabedatum().after(startDatum)
            && ausleihe.getAusleihRueckgabedatum().before(endDatum)) {
            return true;
        }
        if (ausleihe.getAusleihStartdatum().equals(startDatum)
            || ausleihe.getAusleihStartdatum().equals(endDatum)) {
            return true;
        }
        return ausleihe.getAusleihRueckgabedatum().equals(startDatum)
            || ausleihe.getAusleihRueckgabedatum().equals(endDatum);
    }

    /**
     * Ueberprueft ob der der Artikel fuer die angegebene Zeit bereits ausgeliehen ist.
     * @param artikelId
     * @param startDatum
     * @param endDatum
     * @return true, falls der Artikel an mindestens einem der Tage bereits verliehen ist. Sonst false.
     */
    public boolean isAusgeliehen(Long artikelId, Calendar startDatum, Calendar endDatum) {
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        for (Ausleihe ausleihe : artikel.getAusgeliehen()) {
            if (!ausleihe.getAusleihStatus().equals(Status.ANGEFRAGT)
                && !ausleihe.getAusleihStatus().equals(Status.BEENDET)) {

                if (startDatum.before(ausleihe.getAusleihStartdatum())
                    && (endDatum.after(ausleihe.getAusleihStartdatum()))) {
                    return true;
                }
                if (startDatum.before(ausleihe.getAusleihRueckgabedatum())
                    && endDatum.after(ausleihe.getAusleihRueckgabedatum())) {
                    return true;
                }
                if (startDatum.equals(ausleihe.getAusleihStartdatum())
                    || endDatum.equals(ausleihe.getAusleihRueckgabedatum())
                    || startDatum.equals(ausleihe.getAusleihRueckgabedatum())
                    || endDatum.equals(ausleihe.getAusleihStartdatum())) {
                    return true;
                }
                if (startDatum.after(ausleihe.getAusleihStartdatum())
                    && startDatum.before(ausleihe.getAusleihRueckgabedatum())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param liste
     * @return
     */
    public List<Ausleihe> getKonflike(List<Ausleihe> liste) {
        List<Ausleihe> konflikeAusleihe = new ArrayList<>();
        for (Ausleihe a : liste) {
            if (a.getAusleihStatus() == Status.KONFLIKT) {
                konflikeAusleihe.add(a);
            }
        }
        return konflikeAusleihe;
    }
}
