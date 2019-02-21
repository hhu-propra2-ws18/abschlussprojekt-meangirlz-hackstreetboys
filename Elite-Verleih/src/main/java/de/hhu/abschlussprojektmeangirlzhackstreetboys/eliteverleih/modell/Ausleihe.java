package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
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

    public Ausleihe() {
    }

    public Ausleihe(Artikel artikel, Calendar ausleihStartdatum, Calendar ausleihRueckgabedatum, Benutzer benutzer,
                    Status ausleihStatus, int reservationsId) {
        this.artikel = artikel;
        this.ausleihStartdatum = ausleihStartdatum;
        this.ausleihRueckgabedatum = ausleihRueckgabedatum;
        this.benutzer = benutzer;
        this.ausleihStatus = ausleihStatus;
        this.reservationsId = reservationsId;
    }

    public int getAnzahlTage() {
        int ergebnis = 0;
        Calendar start = ausleihStartdatum;
        Date date = new Date(System.currentTimeMillis());
        Calendar dateCal = new GregorianCalendar();
        dateCal.setTime(date);
        if (dateCal.equals(start)) {
            return 1;
        }
        if (dateCal.after(start)) {
            long milli = date.getTime() - start.getTimeInMillis();

            ergebnis = (int) TimeUnit.DAYS.convert(milli, TimeUnit.MILLISECONDS) + 1;
        }
        return ergebnis;
    }
}
