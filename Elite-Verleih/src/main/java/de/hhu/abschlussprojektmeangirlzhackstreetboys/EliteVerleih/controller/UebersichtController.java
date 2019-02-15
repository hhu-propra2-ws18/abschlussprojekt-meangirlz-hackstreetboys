package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UebersichtController {

    @Autowired
    BenutzerManager benutzerManager;
    @Autowired
    ArtikelManager artikelManager;

    @GetMapping("/Uebersicht")
    public String UebersichtAnzeigen(Model model,
                                     Long id){


        Benutzer benutzer = benutzerManager.getBenutzerById(id);

        model.addAttribute("benutzer",benutzer);
        model.addAttribute("artikels", artikelManager.getAllArtikel());


        return "Uebersicht";
    }
}
