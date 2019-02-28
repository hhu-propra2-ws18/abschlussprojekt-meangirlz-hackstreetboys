package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
public class
Benutzer {

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Transaktion> transaktionen;

    public Benutzer() {
    }
}
