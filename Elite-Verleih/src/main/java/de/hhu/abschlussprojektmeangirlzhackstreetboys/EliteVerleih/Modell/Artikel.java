package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Modell;

import lombok.Data;
import javax.persistence.Entity;

import org.springframework.context.annotation.Bean;

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
    private int kaution;
    private int tarif;

    private String artikelOrt;
    private List<Ausleihe> ausleihList;


    public Artikel() {
    }
}
