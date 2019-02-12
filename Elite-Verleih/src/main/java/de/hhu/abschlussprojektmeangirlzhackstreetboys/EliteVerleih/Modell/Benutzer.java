package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Modell;
//import lombok.Data;
import lombok.*;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@Entity
class Benutzer {

    @Id
    Long id;
    String username;
    String emailadresse;
    List<Ausleihe> ausgeliehen;

}
