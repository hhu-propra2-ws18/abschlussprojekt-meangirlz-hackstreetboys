package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Modell;
//import lombok.Data;
import lombok.*;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;



@Data
@Entity
public class Benutzer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String username;
    private String emailadresse;
    private List<Ausleihe> ausgeliehen;

    public Benutzer(){
    }

    public Benutzer(String username, String emailadresse){
        this.username = username;
        this.emailadresse = emailadresse;

        List<Ausleihe> ausl = new ArrayList<Ausleihe>();
        this.ausgeliehen = ausl;

    }

    public Benutzer(String username, String emailadresse, List<Ausleihe> ausgeliehen){
        this.username = username;
        this.emailadresse = emailadresse;
        this.ausgeliehen = ausgeliehen;

    }

}
