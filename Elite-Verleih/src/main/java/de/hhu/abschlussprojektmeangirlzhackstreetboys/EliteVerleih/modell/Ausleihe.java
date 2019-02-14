package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell;

import java.util.Date;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Ausleihe {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long ausleiheId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artikel artikel;

    private Date ausleihStartdatum;

    private Date ausleihRueckgabedatum;

    @ManyToOne(fetch = FetchType.EAGER)
    private Benutzer benutzer;

    public Ausleihe(Artikel artikel, Date ausleihStartdatum, Date ausleihRueckgabedatum, Benutzer benutzer){
        this.artikel = artikel;
        this.ausleihStartdatum = ausleihStartdatum;
        this.ausleihRueckgabedatum = ausleihRueckgabedatum;
        this.benutzer = benutzer;
    }
}
