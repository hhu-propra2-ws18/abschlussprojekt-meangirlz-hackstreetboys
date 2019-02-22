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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
     * Getter fuer alle Ausleihen der Datenbank.
     *
     * @return Liste von Ausleihen.
     */
    public List<Ausleihe> getAllAusleihe() {
        return ausleiheRepo.findAll();
    }

    /**
     * Erstellt eine Ausleihe mit allen Abhaengigkeiten.
     *
     * @param benutzerId            Id des Ausleihenden.
     * @param artikelId             Id des Artikels.
     * @param ausleihStartdatum     Startdatum der Ausleihe.
     * @param ausleihRueckgabedatum Rueckgabedatum der Ausleihe.
     * @return Ausleihe.
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

    private void setzeAusleiheBenutzer(Long benutzerId, Ausleihe ausleihe) {
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        if (b.getAusgeliehen() == null) {
            b.setAusgeliehen(new ArrayList<Ausleihe>());
        }
        b.getAusgeliehen().add(ausleihe);
        benutzerRepo.save(b);
    }

    private void setzeAusleiheArtikel(Long artikelId, Ausleihe ausleihe) {
        Artikel a = artikelRepo.findArtikelByArtikelId(artikelId);
        if (a.getAusgeliehen() == null) {
            a.setAusgeliehen(new ArrayList<Ausleihe>());
        }
        a.getAusgeliehen().add(ausleihe);
        artikelRepo.save(a);
    }

    /**
     * Getter fuer die Ausleihen via ausleiheId.
     *
     * @param ausleiheId Id der Ausleihe.
     * @return Ausleihe.
     */
    public Ausleihe getAusleiheById(Long ausleiheId) {
        return ausleiheRepo.findAusleiheByAusleihId(ausleiheId);
    }

    /**
     * Bestaetigt eine Ausleihe und beachtet die Abhaengigkeiten dabei.
     * Sollte das Datum bei der Bestaetigung, nach dem Startdatum der Anfrage sein, wird die Ausleihe geloescht,
     * da diese so wie angefragt nicht mehr moeglich ist.
     *
     * @param ausleiheId Id der Ausleihe.
     */
    public void bestaetigeAusleihe(Long ausleiheId) {
        Ausleihe ausleihe = getAusleiheById(ausleiheId);
        Calendar heute = new GregorianCalendar();
        if (!ausleihe.gueltigesDatum(heute)) {
            loescheAusleihe(ausleiheId);
            return;
        }

        Artikel artikel = ausleihe.getArtikel();
        ReservationDto r1 = propayManager.kautionReserviern(ausleihe.getBenutzer().getBenutzerName(),
            artikel.getBenutzer().getBenutzerName(), artikel.getArtikelKaution());
        if (r1 == null) {
            bearbeiteAusleihe(ausleiheId, Status.ABGELEHNT);
        } else {
            bearbeiteAusleihe(ausleiheId, Status.BESTAETIGT);
            loescheKollidierendeAnfragen(ausleiheId);
            ausleihe.setReservationsId(r1.getId());
        }

        ausleiheRepo.save(ausleihe);
    }

    /**
     * Loescht die Ausleihe und beachtet die Abhaengigkeiten dabei.
     *
     * @param ausleihId Id der Ausleihe.
     */
    public void loescheAusleihe(Long ausleihId) {
        Ausleihe a = ausleiheRepo.findAusleiheByAusleihId(ausleihId);
        loescheAusleiheFuerBenutzer(a.getBenutzer().getBenutzerId(), a);
        loescheAusleiheFuerArtikelundBesitzer(a.getArtikel().getBenutzer().getBenutzerId(), a.getArtikel(), a);
        ausleiheRepo.delete(a);
    }

    /**
     * Loescht die Ausleihe fuer den Benutzer.
     * Muss mit Ausleihe aus dem Repo augerufen werden
     *
     * @param benutzerId Id des Ausleihenden.
     * @param ausleihe   Id der Ausleihe.
     */
    private void loescheAusleiheFuerBenutzer(Long benutzerId, Ausleihe ausleihe) {
        Benutzer b = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        b.getAusgeliehen().remove(ausleihe);
        benutzerRepo.save(b);
    }

    /**
     * Loescht die Ausleihe fuer den Benutzer und den Artikel.
     *
     * @param benutzerId Id des Artikelbesitzers.
     * @param artikel    Artikel.
     * @param ausleihe   Ausleihe.
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
     * Setzt den Status der Ausleihe wie uebergebn.
     *
     * @param ausleiheId          Id der Ausleihe.
     * @param neuerAusleiheStatus Neuer Status fuer die Ausleihe.
     * @return Ausleihe.
     */
    public Ausleihe bearbeiteAusleihe(Long ausleiheId, Status neuerAusleiheStatus) {
        Ausleihe newA = getAusleiheById(ausleiheId);
        newA.setAusleihStatus(neuerAusleiheStatus);
        return ausleiheRepo.save(newA);
    }

    /**
     * Loescht alle Anfragen, welche sich mit der angegebenen Anfrage zeitlich ueberschneiden.
     *
     * @param ausleiheId Id der Ausleihe.
     */
    private void loescheKollidierendeAnfragen(Long ausleiheId) {
        Artikel artikel = getAusleiheById(ausleiheId).getArtikel();
        List<Ausleihe> ausleihList = artikel.getAusgeliehen();
        List<Ausleihe> anfrageList = new ArrayList<>();
        for (Ausleihe a : ausleihList) {
            if (a.getAusleihStatus().equals(Status.ANGEFRAGT)) {
                anfrageList.add(a);
            }
        }
        for (Ausleihe a : anfrageList) {
            if (a.getAusleihId() != ausleiheId) {
                if (kollidiertMitAusleihe(a.getAusleihId(), ausleiheId)) {
                    bearbeiteAusleihe(a.getAusleihId(), Status.ABGELEHNT);
                }
            }
        }
        for (Ausleihe a : anfrageList) {
            if (a.getAusleihStatus().equals(Status.ABGELEHNT)) {
                loescheAusleihe(ausleiheId);
            }
        }
    }

    /**
     * Ueberprueft ob sich die beiden ausleihen ueberschneiden.
     *
     * @param ausleiheId     Id der ersten Ausleihe.
     * @param akzeptierteAId Id der anderen Ausleihe.
     * @return boolean ob die Ausleihen miteinander kollidieren.
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
     *
     * @param artikelId  Id des Artikels.
     * @param startDatum Startdatum der zu ueberpruefenden Anfrage.
     * @param endDatum   Enddatum der zu ueberpruefenden Anfrage.
     * @return true, falls der Artikel an mindestens einem der Tage bereits verliehen ist. Sonst false.
     */
    public boolean isAusgeliehen(Long artikelId, Calendar startDatum, Calendar endDatum) {
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        for (Ausleihe ausleihe : artikel.getAusgeliehen()) {
            if (!ausleihe.getAusleihStatus().equals(Status.ANGEFRAGT)
                && !ausleihe.getAusleihStatus().equals(Status.BEENDET)
                && !ausleihe.getAusleihStatus().equals(Status.ABGELEHNT)) {

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
     * Getter fuer alle Konflikte des Benutzers.
     *
     * @param liste Liste von Ausleihen.
     * @return Liste von Ausleihen, welche alle den Status KONFLIKT haben.
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

    /**
     * Bearbeitet das zurueckgeben einer Ausleihe und das Bezahlen dieser.
     * Ist nicht genug Geld bei der Rueckgabe vorhanden wird die Ausleihe auf KONFLIKT gesetzt.
     *
     * @param ausleiheId Id der Ausleihe welche zurueckgegeben wird.
     */
    public void zurueckGeben(Long ausleiheId) {
        Ausleihe ausleihe = getAusleiheById(ausleiheId);
        Calendar heute = new GregorianCalendar();
        int kosten = ausleihe.berechneKosten(heute);
        if (propayManager.ueberweisen(ausleihe.getBenutzer().getBenutzerName(),
            ausleihe.getArtikel().getBenutzer().getBenutzerName(),
            kosten)) {
            bearbeiteAusleihe(ausleiheId, Status.ABGEGEBEN);
        } else {
            bearbeiteAusleihe(ausleiheId, Status.KONFLIKT);
        }
    }

    public void rueckgabeAkzeptieren(Long ausleihId) {
        bearbeiteAusleihe(ausleihId, Status.BEENDET);
        Ausleihe ausleihe = getAusleiheById(ausleihId);
        propayManager.kautionFreigeben(ausleihe.getBenutzer().getBenutzerName(), ausleihe.getReservationsId());
    }
}
