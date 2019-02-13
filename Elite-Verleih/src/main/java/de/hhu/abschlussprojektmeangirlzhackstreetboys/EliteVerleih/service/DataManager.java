package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DataManager {

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    ArtikelRepository artikelRepo;

    public List<Benutzer> getAllBenutzer() {
        return benutzerRepo.findAll();
    }

    public List<Artikel> getAllArtikel() {
        return artikelRepo.findAll();
    }

    public boolean nameSchonVorhanden(String name){
        List<Benutzer> alleBenutzer = getAllBenutzer();

        for(Benutzer benutzer: alleBenutzer){
            if(benutzer.getBenutzerName() == name){
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

        benutzerRepo.saveAll(Arrays.asList(benutzer));
        return benutzer;
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

   public void erstelleArtikel(Long benutzerId, Artikel artikel){
        Benutzer benutzer = benutzerRepo.findBenutzerByBenutzerId(benutzerId);
        artikel.setBenutzer(benutzer);
        artikelRepo.saveAll(Arrays.asList(artikel));
    }

    public Artikel getArtikelById(Long artikelId){
        return artikelRepo.findArtikelByArtikelId(artikelId);
    }

    void bearbeiteArtikel(Long artikelId, Artikel artikel){
        Artikel alterArtikel = getArtikelById(artikelId);

        alterArtikel.setArtikelBeschreibung(artikel.getArtikelBeschreibung());
        alterArtikel.setArtikelKaution(artikel.getArtikelKaution());
        alterArtikel.setArtikelName(artikel.getArtikelName());
        alterArtikel.setArtikelOrt(artikel.getArtikelOrt());
        alterArtikel.setArtikelTarif(artikel.getArtikelTarif());

        artikelRepo.saveAll(Arrays.asList(alterArtikel));
    }

}



