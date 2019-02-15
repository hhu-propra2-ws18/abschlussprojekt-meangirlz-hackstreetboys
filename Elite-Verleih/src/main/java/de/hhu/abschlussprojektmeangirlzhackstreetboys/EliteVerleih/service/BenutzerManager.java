package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BenutzerManager {

    @Autowired
    BenutzerRepository benutzerRepo;

    public List<Benutzer> getAllBenutzer() {
        return benutzerRepo.findAll();
    }

    public boolean nameSchonVorhanden(String name){
        List<Benutzer> alleBenutzer = getAllBenutzer();

        for(Benutzer benutzer: alleBenutzer){
            if(benutzer.getBenutzerName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public Benutzer getBenutzerById(Long benutzerId) {
        return benutzerRepo.findBenutzerByBenutzerId(benutzerId);
    }

    public Benutzer erstelleBenutzer(Benutzer benutzer) {
        if(nameSchonVorhanden(benutzer.getBenutzerName())) return null;

        return benutzerRepo.save(benutzer);
    }

    public Benutzer findBenutzerByName(String name){

        if(!benutzerRepo.findBenutzerByBenutzerName(name).isPresent()) return null;

        return benutzerRepo.findBenutzerByBenutzerName(name).get();
    }

    public void bearbeiteBenutzer(Long benutzerId, Benutzer benutzer) {
        Benutzer alterBenutzer = getBenutzerById(benutzerId);

        alterBenutzer.setBenutzerEmail(benutzer.getBenutzerEmail());

        benutzerRepo.saveAll(Arrays.asList(alterBenutzer));
    }

    //TODO: Löschen!
	public Benutzer editBenutzer(Benutzer benutzer, String email) {
        benutzer.setBenutzerEmail(email);
        benutzerRepo.save(benutzer);
		return benutzer;
	}

	public List<Ausleihe> sucheAnfragen(Benutzer benutzer){

        List<Ausleihe> wartend = new ArrayList<>();
        for( Artikel a: benutzer.getArtikel() ) {
            for (Ausleihe b: a.getAusgeliehen() ){
                System.out.println(b.getAusleihStatus());
                if (b.getAusleihStatus()== Status.ANGEFRAGT){
                    wartend.add(b);
                }
            }
        }
        return wartend;
    }


}



