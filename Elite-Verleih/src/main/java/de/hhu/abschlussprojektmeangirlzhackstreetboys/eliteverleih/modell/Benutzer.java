package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;
//import lombok.Data;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long benutzerId;

    private String benutzerName;

    private String benutzerEmail;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Artikel> artikel;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Ausleihe> ausgeliehen;

    private String benutzerPasswort;

    private String benutzerRolle;

    public Benutzer() {
    }

    public Benutzer(String benutzerName, String benutzerEmail) {
        this.benutzerName = benutzerName;
        this.benutzerEmail = benutzerEmail;

        List<Ausleihe> ausl = new ArrayList<Ausleihe>();
        this.ausgeliehen = ausl;

    }

    public Benutzer(String benutzerName, String benutzerEmail, List<Ausleihe> ausgeliehen) {
        this.benutzerName = benutzerName;
        this.benutzerEmail = benutzerEmail;
        this.ausgeliehen = ausgeliehen;

    }

}
