package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.AusleiheManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Controller
public class KonfliktController {

    @Autowired
    BenutzerManager benutzerManager;

    @Autowired
    AusleiheManager ausleiheManager;

    @Autowired
    ArtikelManager artikelManager;

    @GetMapping("/Konfliktloesung")
    public String ProfilAnzeigen(Long id, Model model){
        if(id != 1) {
            return "redirect:/";
        }
        /*
        //alle Benutzer
        //List<Ausleihe> ausleihe = ausleiheManager.

        Benutzer benutzer = benutzerManager.getBenutzerById(id);
        model.addAttribute("benutzer",benutzer);

        //model.addAttribute("konflikt", benutzerManager.sucheAnfragen(benutzer, new String("KONFLIKT")));
        */
        return "Konfliktloesung";
    }
}
