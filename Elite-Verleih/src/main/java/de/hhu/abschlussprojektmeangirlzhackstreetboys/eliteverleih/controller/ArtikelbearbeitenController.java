package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ArtikelbearbeitenController {

    @Autowired
    BenutzerManager benutzerManager;

    @Autowired
    ArtikelManager artikelManager;

    /**
     * Zeigt Artikel bearbeiten view an, laedt Attribute in HTML.
     *
     * @param model   Datencontainer fuer View.
     * @param account aktueller Benutzer.
     * @return Mapping auf Artikel bearbeiten.
     */
    @GetMapping("/Bearbeiten/{artikelId}")
    public String artikelBearbeitungAnzeigen(@PathVariable long artikelId,
                                             Model model,
                                             Principal account) {

        model.addAttribute("artikel", artikelManager.getArtikelById(artikelId));
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));
        return "Artikelbearbeiten";
    }

    /**
     * Speichert die neuen Attribute des Artikels.
     *
     * @param model      Datencontainer fuer die View.
     * @param artikelId  Eindeutige ID des Artikels.
     * @param newArtikel Bearbeiteter Artikel.
     * @param account    Aktive Benutzer.
     * @return Uebersicht seite.
     */
    @PostMapping("/Bearbeiten/{artikelId}")
    public String artikelBearbeiten(Model model,
                                    @PathVariable long artikelId,
                                    @ModelAttribute Artikel newArtikel,
                                    Principal account) {
        List<Ausleihe> ausleihen = artikelManager.getArtikelById(artikelId).getAusgeliehen();
        boolean aktiveAusleiheVorhanden = false;
        for (Ausleihe ausleihe : ausleihen) {

            if (ausleihe.getAusleihStatus() == Status.BESTAETIGT || ausleihe.getAusleihStatus() == Status.ANGEFRAGT) {
                aktiveAusleiheVorhanden = true;
            }
        }

        if (!aktiveAusleiheVorhanden) {
            artikelManager.bearbeiteArtikel(artikelId, newArtikel);
            return "redirect:/Uebersicht";
        } else {
            return "redirect:/ErrorBearbeitung";
        }

    }

    /**
     * Prueft, ob Ausleihen bestehen und loescht falls nicht.
     *
     * @param artikelId Eindeutige ID des Artikels.
     * @param account   Aktive Benutzer.
     * @return Uebersicht beim Erfolgreichen loeschen und Error falls nicht.
     */
    @RequestMapping("/Loeschen/{artikelId}")
    public String artikelLoeschen(@PathVariable long artikelId,
                                  Principal account) {
        if (artikelManager.getArtikelById(artikelId).getAusgeliehen().isEmpty()) {
            artikelManager.loescheArtikel(artikelId);
            return "redirect:/Uebersicht";
        }
        return "redirect:/Bearbeiten/" + artikelId + "?error";
    }

    /**
     * Geht auf die Error Seite wenn ein Artikel bearbeitet wird waehrend eine Ausleihe besteht.
     *
     * @param model   Das zu uebergebende Model
     * @param account Der account des Benutzers
     * @return Gibt einen Error Seite zurueck
     */
    @GetMapping("/ErrorBearbeitung")
    public String errorBearbeitung(Model model, Principal account) {
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));

        return "ErrorBearbeitung";
    }
}
