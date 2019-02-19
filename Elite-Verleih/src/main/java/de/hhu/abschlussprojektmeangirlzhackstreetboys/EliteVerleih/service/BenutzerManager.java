package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller.DataSync;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dto.AccountDTO;
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

    DataSync sync = new DataSync();

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
        AccountDTO account = sync.getAccount(benutzer.getBenutzerName());


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

	public List<Ausleihe> sucheAnfragen(Benutzer benutzer, Status status){

        List<Ausleihe> wartend = new ArrayList<>();
        for( Artikel a: benutzer.getArtikel() ) {
            for (Ausleihe b: a.getAusgeliehen() ){
                if (b.getAusleihStatus().equals(status)){
                    wartend.add(b);
                }
            }
        }
        return wartend;
    }

	public List<Ausleihe> sucheEigeneAnfragen(Benutzer benutzer, Status status) {
		List<Ausleihe> list = new ArrayList<Ausleihe>();
		for (Ausleihe b : benutzer.getAusgeliehen()) {
    		if(b.getAusleihStatus().equals(status)) {
    			list.add(b);
    		}
    	}
		return list;
	}


}



