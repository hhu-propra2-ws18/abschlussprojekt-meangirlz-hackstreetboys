package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
public class ArtikelbearbeitenController {

    @Autowired
    ArtikelRepository artikelRepo;

    @Autowired
    BenutzerManager benutzerManager;

    @GetMapping("/Bearbeiten/{artikelId}")
    public String bearbeiteArtikel(@PathVariable long artikelId, Model model, Long id ){

        model.addAttribute("artikel", artikelRepo.findArtikelByArtikelId(artikelId));
        model.addAttribute("benutzer", benutzerManager.getBenutzerById(id));
        return "Artikelbearbeiten";
    }

    @PostMapping("/Bearbeiten/{artikelId}")
    public String personSubmitStart(@PathVariable long artikelId, @ModelAttribute Artikel newArtikel, Model model, Long id) {
        Benutzer benutzer = benutzerManager.getBenutzerById(id);
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);

        model.addAttribute("artikel", artikelRepo.findArtikelByArtikelId(artikelId));
        model.addAttribute("benutzer", benutzer);
        bearbeiteArtikel(newArtikel, artikel);
        artikel.setBenutzer(benutzer);
        artikelRepo.save(artikel);
        return "redirect:/Uebersicht?id=" + benutzer.getBenutzerId();
    }

    @RequestMapping("/Loeschen/{artikelId}")
    public String delete(@PathVariable long artikelId, Long id){
        Benutzer benutzer = benutzerManager.getBenutzerById(id);
        //Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        artikelRepo.deleteById(artikelId);
        return "redirect:/Uebersicht?id=" + benutzer.getBenutzerId();
    }

    private void bearbeiteArtikel(Artikel newArtikel, Artikel oldArtikel){
        oldArtikel.setArtikelName(newArtikel.getArtikelName());
        oldArtikel.setArtikelBeschreibung(newArtikel.getArtikelBeschreibung());
        oldArtikel.setBenutzer(newArtikel.getBenutzer());
        oldArtikel.setArtikelKaution(newArtikel.getArtikelKaution());
        oldArtikel.setArtikelTarif(newArtikel.getArtikelTarif());
        oldArtikel.setArtikelOrt(newArtikel.getArtikelOrt());

        artikelRepo.saveAll(Arrays.asList(oldArtikel));

    }

}
