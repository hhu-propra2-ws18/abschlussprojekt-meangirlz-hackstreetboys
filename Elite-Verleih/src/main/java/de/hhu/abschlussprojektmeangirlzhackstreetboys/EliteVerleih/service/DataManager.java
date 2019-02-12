package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Benutzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataManager {

    @Autowired
    BenutzerRepository benutzerRep;

    public List<Benutzer> getAllBenutzer(){
        return benutzerRep.findAll();
    }

    public Benutzer getBenutzerById(Long benutzerId){return benutzerRep.findBenutzerByBenutzerId(benutzerId);}

    public Benutzer erstellen(String benutzerName, String benutzerEmail){
        Benutzer benutzer = new Benutzer(benutzerName,benutzerEmail);
        benutzerRep.save(benutzer);
        return benutzer;
    }
}
