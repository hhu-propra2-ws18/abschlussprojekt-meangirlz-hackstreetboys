package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Modell;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;

import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Artikel {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    private int artikelId;
    private String artikelName;
    private String artikelBeschreibung;
    private Benutzer artikelBenutzer;
    private int artikelKaution;
    private int artikelTarif;

    private String artikelOrt;
    private List<Ausleihe> ausleiheList;


    public Artikel() {
    }

    public Artikel(String artikelName, String artikelBeschreibung, Benutzer artikelBenutzer, int artikelKaution,
                   int artikelTarif, String artikelOrt){
        this.artikelName = artikelName;
        this.artikelBeschreibung = artikelBeschreibung;
        this.artikelBenutzer = artikelBenutzer;
        this.artikelKaution = artikelKaution;
        this.artikelTarif = artikelTarif;
        this.artikelOrt = artikelOrt;
        List<Ausleihe> ausl = new ArrayList<Ausleihe>();
        this.ausleiheList = ausl;

    }

    public Artikel(String artikelName, String artikelBeschreibung, Benutzer artikelBenutzer,
                   int artikelKaution, int artikelTarif, String artikelOrt, List<Ausleihe> ausleiheList){
        this.artikelName = artikelName;
        this.artikelBeschreibung = artikelBeschreibung;
        this.artikelBenutzer = artikelBenutzer;
        this.artikelKaution = artikelKaution;
        this.artikelTarif = artikelTarif;
        this.artikelOrt = artikelOrt;
        this.ausleiheList = ausleiheList;

    }

    public void addAusleihe(Ausleihe ausleihe){
        if(ausleiheList == null) ausleiheList = new ArrayList<Ausleihe>();
        this.ausleiheList.add(ausleihe);
    }


}
