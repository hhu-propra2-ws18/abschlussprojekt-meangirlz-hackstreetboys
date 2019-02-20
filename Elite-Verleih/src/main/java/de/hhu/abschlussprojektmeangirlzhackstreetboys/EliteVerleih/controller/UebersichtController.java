package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UebersichtController {

    @Autowired
    BenutzerManager benutzerManager;
    @Autowired
    ArtikelManager artikelManager;

    @GetMapping("/Uebersicht")
    public String uebersichtAnzeigen(Model model, Long id, String suche){

        Benutzer benutzer = benutzerManager.getBenutzerById(id);

        List<Artikel> sortiertelListe = new ArrayList<>();

        if (suche != null) {
            sortiertelListe = artikelManager.getArtikelListSortByName(suche);
        }
        else {
            sortiertelListe = artikelManager.getArtikelListSortByName("");
        }

        model.addAttribute("benutzer",benutzer);
        model.addAttribute("artikels", sortiertelListe);

        return "Uebersicht";
    }

    @PostMapping("/Uebersicht")
    public String sucheArtikel(Model model, Long id,
                               @RequestParam(value = "sucheButton") String sucheButton,
                               @RequestParam(required = false) String suchBegriff) {
        if (suchBegriff != null) {
            return "redirect:/Uebersicht?id=" + id + "&suche=" + suchBegriff;
        }
        else {
            return "redirect:/Uebersicht?id=" + id;
        }
    }
}
