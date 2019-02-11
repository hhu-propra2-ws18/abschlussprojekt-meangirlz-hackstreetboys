package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Modell;

import java.util.Date;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@Entity
class Ausleihe {

    @Id
    private Long id;
    private Artikel artikel;
    private Date startdatum;
    private Date rueckgabedatum;
    private Benutzer benutzer;

}
