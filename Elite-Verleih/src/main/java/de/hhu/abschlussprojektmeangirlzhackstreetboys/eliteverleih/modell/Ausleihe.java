package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;


@Data
@Entity
public class Ausleihe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ausleihId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artikel artikel;

    //private Calendar ausleihStartdatum;
    private Calendar ausleihStartdatum;

    private Calendar ausleihRueckgabedatum;

    @ManyToOne(fetch = FetchType.EAGER)
    private Benutzer benutzer;

    private Status ausleihStatus;

    private int reservationsId;

    /**
     * Konstruktor.
     */
    public Ausleihe() {
    }

    /**
     * Konstruktor.
     *
     * @param artikel Artikel welcher ausgeliehen wird.
     * @param ausleihStartdatum Startdatum der Ausleihe.
     * @param ausleihRueckgabedatum Rueckgabedatum der Ausleihe.
     * @param benutzer Ausleihender.
     * @param ausleihStatus Status der Ausleihe.
     * @param reservationsId Id der Reservierung.
     */
    public Ausleihe(Artikel artikel, Calendar ausleihStartdatum, Calendar ausleihRueckgabedatum, Benutzer benutzer,
                    Status ausleihStatus, int reservationsId) {
        this.artikel = artikel;
        this.ausleihStartdatum = ausleihStartdatum;
        this.ausleihRueckgabedatum = ausleihRueckgabedatum;
        this.benutzer = benutzer;
        this.ausleihStatus = ausleihStatus;
        this.reservationsId = reservationsId;
    }

    /**
     * Berechnet die Anzahl der Tage welche vom Startdatum zu heute vergangen sind.
     * Falls die Daten uebereinstimmen, wird 1 zurueckgegeben.
     *
     * @return Anzahl der Tage welche abgedeckt werden.
     */
    public int getAnzahlTage(Calendar heute) {
        int ergebnis = 0;
        Calendar start = getAusleihStartdatum();
        if (heute.equals(start)) {
            return 1;
        }
        if (heute.after(start)) {

            long milli = heute.getTimeInMillis() - start.getTimeInMillis();

            long diff = TimeUnit.DAYS.convert(milli, TimeUnit.MILLISECONDS) + 2;
            // Start und Ende werden nicht mitbeachtet, weswegen wir +2 rechnene muessen.

            ergebnis = (int) diff;
        }
        return ergebnis;
    }

    /**
     * Ueberprueft ob das Datum zur Ausleihe noch gueltig ist.
     * Ungueltig ist es, sobald die Anfrage nach Beginn der Ausleihe angenommen wird.
     * Wird die Anfrage zu Beginn der Ausleihe angenommen (am gleichen Tag) ist sie noch gueltig.
     *
     * @return true, falls das Datum noch gueltig ist.
     */
    public boolean gueltigesDatum(Calendar jetzt) {
        Calendar start = getAusleihStartdatum();
        Calendar ende = getAusleihRueckgabedatum();
        if(jetzt.after(ende) || jetzt.after(start)){
            return false;
        }

        return true;
    }

    /**
     * Berechnet die Kosten der Rueckgabe. Dabei wird ein Strafsatz von 20% auf den
     * Tarif genommen fuer jeden ueberzogenen Tag (aufgerundet zu integer).
     *
     * @return Kosten.
     */
    public int berechneKosten(Calendar jetzt) {
        int tage = getAnzahlTage(jetzt);
        if(tage == 0){
            return 0;
        }
        int ueberzogen = getAnzahlUeberzogen(jetzt);
        double kosten = this.getArtikel().getArtikelTarif()*(tage + ueberzogen*0.2);
        int endKosten = (int) Math.ceil(kosten);
        return (endKosten);
    }

    /**
     * Berechnet die Anzahl der Tage die seit dem vereinbarten Rueckgabedatum vergangen sind.
     *
     * @return Anzahl der Tage die ueberzogen worden sind.
     */
    public int getAnzahlUeberzogen(Calendar jetzt) {
        Calendar ende = getAusleihRueckgabedatum();
        if(!jetzt.after(ende)){
            return 0;
        }
        long milli = jetzt.getTimeInMillis() - ende.getTimeInMillis();
        int ergebnis = (int) TimeUnit.DAYS.convert(milli, TimeUnit.MILLISECONDS) + 1;
        return ergebnis;
    }
}
