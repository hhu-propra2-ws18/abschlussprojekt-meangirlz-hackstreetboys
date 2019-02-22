package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Artikel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long artikelId;

    private String artikelName;

    @Column(columnDefinition = "TEXT")
    private String artikelBeschreibung;

    @ManyToOne(fetch = FetchType.LAZY)
    private Benutzer benutzer;

    private int artikelKaution;

    private int artikelTarif;

    private String artikelOrt;

    private int artikelOrtX;
    private int artikelOrtY;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Ausleihe> ausgeliehen;

    private String artikelBildUrl;

    public Artikel() {
    }

    public Artikel(String artikelName, String artikelBeschreibung, Benutzer benutzer, int artikelKaution,
                   int artikelTarif, String artikelOrt, String artikelBildUrl) {
        this.artikelName = artikelName;
        this.artikelBeschreibung = artikelBeschreibung;
        this.benutzer = benutzer;
        this.artikelKaution = artikelKaution;
        this.artikelTarif = artikelTarif;
        this.artikelOrt = artikelOrt;
        List<Ausleihe> ausl = new ArrayList<Ausleihe>();
        this.ausgeliehen = ausl;
        this.artikelBildUrl = artikelBildUrl;
    }

    public Artikel(String artikelName, String artikelBeschreibung, Benutzer benutzer, int artikelKaution,
                   int artikelTarif, String artikelOrt, List<Ausleihe> ausleiheList, String artikelBildUrl) {
        this.artikelName = artikelName;
        this.artikelBeschreibung = artikelBeschreibung;
        this.benutzer = benutzer;
        this.artikelKaution = artikelKaution;
        this.artikelTarif = artikelTarif;
        this.artikelOrt = artikelOrt;
        this.ausgeliehen = ausleiheList;
        this.artikelBildUrl = artikelBildUrl;
    }
}
