package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataManager {

    @Autowired
    BenutzerRepository benutzerRepo;

    public List<Benutzer> getAllBenutzer(){
        return benutzerRepo.findAll();
    }

    public Benutzer getBenutzerById(Long benutzerId){return benutzerRepo.findBenutzerByBenutzerId(benutzerId);}

    public Benutzer erstellen(String benutzerName, String benutzerEmail){
        Benutzer benutzer = new Benutzer(benutzerName,benutzerEmail);
        benutzerRepo.save(benutzer);
        return benutzer;
    }
}
