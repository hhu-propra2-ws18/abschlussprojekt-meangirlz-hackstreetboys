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

    /**
     * Zeigt Artikel bearbeiten view an, laedt Attribute in HTML.
     * @param model  Datencontainer fuer View.
     * @param account aktueller Benutzer.
     * @return Mapping auf Artikel bearbeiten.
     */
    @GetMapping("/Bearbeiten/{artikelId}")
    public String artikelBearbeitungAnzeigen(@PathVariable long artikelId,
                                             Model model,
                                             Principal account) {

        model.addAttribute("artikel", artikelRepo.findArtikelByArtikelId(artikelId));
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));
        return "Artikelbearbeiten";
    }

    /**
     * Speichert die neuen Attribute des Artikels.
     * @param model Datencontainer fuer die View.
     * @param artikelId Eindeutige ID des Artikels.
     * @param newArtikel Bearbeiteter Artikel.
     * @param account Aktive Benutzer.
     * @return Uebersicht seite.
     */
    @PostMapping("/Bearbeiten/{artikelId}")
    public String artikelBearbeiten(Model model,
                                    @PathVariable long artikelId,
                                    @ModelAttribute Artikel newArtikel,
                                    Principal account) {
        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        Artikel artikel = artikelRepo.findArtikelByArtikelId(artikelId);
        model.addAttribute("artikel", artikelRepo.findArtikelByArtikelId(artikelId));
        model.addAttribute("benutzer", benutzer);
        artikelManager.bearbeiteArtikel(artikelId, artikel);
        artikel.setBenutzer(benutzer);
        artikelRepo.save(artikel);
        return "redirect:/Uebersicht";
    }

    /**
     * Prueft, ob Ausleihen bestehen und loescht falls nicht.
     * @param artikelId Eindeutige ID des Artikels.
     * @param account Aktive Benutzer.
     * @return Uebersicht beim Erfolgreichen loeschen und Error falls nicht.
     */
    @RequestMapping("/Loeschen/{artikelId}")
    public String artikelLoeschen(@PathVariable long artikelId,
                                  Principal account) {
        if (artikelManager.getArtikelById(artikelId).getAusgeliehen().isEmpty()) {
            artikelManager.deleteArtikel(artikelId);
            return "redirect:/Uebersicht";
        }
        return  "redirect:/Bearbeiten/" + artikelId + "?error";
    }
}
