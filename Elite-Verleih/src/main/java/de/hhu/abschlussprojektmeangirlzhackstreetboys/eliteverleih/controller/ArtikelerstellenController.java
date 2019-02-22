package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ArtikelerstellenController {

    @Autowired
    ArtikelRepository artikelRepository;

    @Autowired
    BenutzerManager benutzerManager;

    /**
     * GetMapping der Artikelerstellen Seite.
     *
     * @param model   Das Model.
     * @param account Der Account des Benutzers.
     * @return "Artikelerstellung".
     */
    @GetMapping("/Erstellen")
    public String artikelErstellungAnzeigen(Model model, Principal account) {
        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        model.addAttribute("artikel", new Artikel());
        model.addAttribute("benutzer", benutzer);
        return "Artikelerstellung";
    }

    /**
     * PostMapping der Artikelerstellen Seite.
     *
     * @param artikel Der Artikel.
     * @param model   Das Model.
     * @param account Der Account des Benutzers.
     * @return "Uebersicht".
     */
    @PostMapping("/Erstellen")
    public String artikelErstellen(@ModelAttribute Artikel artikel, Model model, Principal account) {
        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        artikel.setBenutzer(benutzer);
        benutzer.getArtikel().add(artikel);
        artikelRepository.save(artikel);
        model.addAttribute("artikel", artikelRepository.findAll());
        model.addAttribute("benutzer", benutzer);
        return "redirect:/Uebersicht";
    }

}
