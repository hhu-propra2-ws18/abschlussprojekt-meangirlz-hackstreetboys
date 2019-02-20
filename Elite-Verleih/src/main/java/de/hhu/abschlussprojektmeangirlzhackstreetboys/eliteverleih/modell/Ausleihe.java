package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;


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
}
