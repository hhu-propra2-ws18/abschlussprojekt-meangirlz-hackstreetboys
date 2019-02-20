package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.AusleiheRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ArtikelbearbeitenController {

    @Autowired
    ArtikelRepository artikelRepo;

    @Autowired
    BenutzerManager benutzerManager;

    @Autowired
    ArtikelManager artikelManager;

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    AusleiheRepository ausleiheRepo;

    @GetMapping("/Bearbeiten/{artikelId}")
    public String artikelBearbeitungAnzeigen(@PathVariable long artikelId, Model model, Principal account){

        model.addAttribute("artikel", artikelRepo.findArtikelByArtikelId(artikelId));
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));
        return "Artikelbearbeiten";
    }

    @PostMapping("/Bearbeiten/{artikelId}")
    public String artikelBearbeiten(@PathVariable long artikelId, @ModelAttribute Artikel newArtikel, Model model, Principal account) {
        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        model.addAttribute("artikel", artikelRepo.findArtikelByArtikelId(artikelId));
        model.addAttribute("benutzer", benutzer);
        bearbeiteArtikel(newArtikel, artikel);
        artikel.setBenutzer(benutzer);
        artikelRepo.save(artikel);
        return "redirect:/Uebersicht";
    }

    @RequestMapping("/Loeschen/{artikelId}")
    public String artikelLoeschen(@PathVariable long artikelId, Principal account){
        artikelManager.deleteArtikel(artikelId);
        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        return "redirect:/Uebersicht";
    }

    private void bearbeiteArtikel(Artikel newArtikel, Artikel oldArtikel){
        oldArtikel.setArtikelName(newArtikel.getArtikelName());
        oldArtikel.setArtikelBeschreibung(newArtikel.getArtikelBeschreibung());
        oldArtikel.setBenutzer(newArtikel.getBenutzer());
        oldArtikel.setArtikelKaution(newArtikel.getArtikelKaution());
        oldArtikel.setArtikelTarif(newArtikel.getArtikelTarif());
        oldArtikel.setArtikelOrt(newArtikel.getArtikelOrt());

        artikelRepo.save(oldArtikel);

    }

}
