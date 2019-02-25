package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UebersichtController {

    @Autowired
    BenutzerManager benutzerManager;
    @Autowired
    ArtikelManager artikelManager;

    /**
     * Zeigt Uebersicht View an und laedt Attribute in HTML.
     *
     * @param model Datencontainer fuer die View.
     * @return Mapping auf Uebersicht.
     */
    @GetMapping("/**")
    public String anzeigen(Model model) {
        return "redirect:/Uebersicht";
    }

    /**
     * Zeigt die Uebersicht mit der aktiven Suche an.
     *
     * @param model   Datencontainer fuer die View.
     * @param suche   Der Suchbegriff.
     * @param account Aktive Benutzer.
     * @return Mapping auf Uebersicht.
     */
    @GetMapping("/Uebersicht")
    public String uebersichtAnzeigen(Model model, String suche, Principal account) {

        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());

        List<Artikel> sortiertelListe = new ArrayList<>();

        if (suche != null) {
            sortiertelListe = artikelManager.getArtikelListSortByName(suche);
        } else {
            sortiertelListe = artikelManager.getArtikelListSortByName("");
        }

        model.addAttribute("benutzer", benutzer);
        model.addAttribute("artikels", sortiertelListe);
        model.addAttribute("suchBegriff", suche);

        return "Uebersicht";
    }

    /**
     * Ruft die Uebersicht auf und setzt den Parameter suche.
     *
     * @param model       Datencontainer fuer die View.
     * @param suchBegriff Der Suchbegriff.
     * @param account     Aktive Benutzer.
     * @return Mapping auf Uebersicht.
     */
    @PostMapping("/Uebersicht")
    public String sucheArtikel(Model model,
                               @RequestParam(required = false) String suchBegriff,
                               Principal account) {
        if (suchBegriff != null) {
            return "redirect:/Uebersicht" + "?suche=" + suchBegriff;
        } else {
            return "redirect:/Uebersicht";
        }
    }
}
