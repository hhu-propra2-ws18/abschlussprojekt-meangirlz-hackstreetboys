package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Modell;

import java.util.Date;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@Entity
public class Ausleihe {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Artikel artikel;
    private Date startdatum;
    private Date rueckgabedatum;
    private Benutzer benutzer;

    public Ausleihe(Artikel artikel, Date startdatum, Date rueckgabedatum, Benutzer benutzer){
        this.artikel = artikel;
        this.startdatum = startdatum;
        this.rueckgabedatum = rueckgabedatum;
        this.benutzer = benutzer;
    }
}
