package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Artikel {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long artikelId;

    private String artikelName;

    private String artikelBeschreibung;

    @ManyToOne(fetch = FetchType.LAZY)
    private Benutzer benutzer;

    private int artikelKaution;

    private int artikelTarif;

    private String artikelOrt;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Ausleihe> ausgeliehen;


    public Artikel() {
    }

    public Artikel(String artikelName, String artikelBeschreibung, Benutzer benutzer, int artikelKaution,
                   int artikelTarif, String artikelOrt){
        this.artikelName = artikelName;
        this.artikelBeschreibung = artikelBeschreibung;
        this.benutzer = benutzer;
        this.artikelKaution = artikelKaution;
        this.artikelTarif = artikelTarif;
        this.artikelOrt = artikelOrt;
        List<Ausleihe> ausl = new ArrayList<Ausleihe>();
        this.ausgeliehen= ausl;

    }

    public Artikel(String artikelName, String artikelBeschreibung, Benutzer benutzer,
                   int artikelKaution, int artikelTarif, String artikelOrt, List<Ausleihe> ausleiheList){
        this.artikelName = artikelName;
        this.artikelBeschreibung = artikelBeschreibung;
        this.benutzer = benutzer;
        this.artikelKaution = artikelKaution;
        this.artikelTarif = artikelTarif;
        this.artikelOrt = artikelOrt;
        this.ausgeliehen = ausleiheList;

    }
}
