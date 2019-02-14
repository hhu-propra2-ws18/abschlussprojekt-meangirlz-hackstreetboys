package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.AusleiheManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class DetailansichtController {

    @Autowired
    AusleiheManager ausleiheManager;

    @Autowired
    ArtikelManager artikelManager;

    @Autowired
    BenutzerManager benutzerManager;


    @GetMapping("/Detailansicht/{artikelId}")
    public String DetailansichtAnzeigen(@PathVariable Long artikelId, Model model, Long id){
        model.addAttribute("artikel", artikelManager.getArtikelById(artikelId));
        model.addAttribute("benutzer", benutzerManager.getBenutzerById(id));
        return "Detailansicht";
    }

    @PostMapping("/Detailansicht/{artikelId}")
    public String erstelleAusleihe(@RequestParam(required = false) Date startDatum,
                        @RequestParam(required = false) Date endDatum,
                        @PathVariable Long artikelId, Long id){

        Benutzer b = benutzerManager.getBenutzerById(id);
        Artikel artikel = artikelManager.getArtikelById(artikelId);
        System.err.println("ENDDATUM" + endDatum);
        System.err.println("Artikel: "+artikel.getArtikelName());
        System.err.println("Benutzerid: "+b.getBenutzerId());

        ausleiheManager.erstelleAusleihe(b.getBenutzerId(),artikel.getArtikelId(),startDatum,endDatum);

        System.err.println("AUSLEIHE ERSTELLEN");
        return "redirect:/Uebersicht?id=" + b.getBenutzerId();
    }





}
