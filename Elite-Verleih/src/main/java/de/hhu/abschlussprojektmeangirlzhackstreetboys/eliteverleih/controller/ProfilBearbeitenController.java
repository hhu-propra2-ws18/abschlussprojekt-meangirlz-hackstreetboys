package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.PropayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ProfilBearbeitenController {

    @Autowired
    BenutzerManager benutzerManager;

    PropayManager propayManager = new PropayManager();

    /**
     * Kuemmert sich um das korrekte Anzeigen der Profilbearbeiten Seite.
     *
     * @param model   Das model.
     * @param account Der Account des aktuellen Benutzers.
     * @return "ProfilBearbeiten".
     */
    @GetMapping("/ProfilBearbeiten")
    public String profilBearbeitenAnzeigen(Model model, Principal account) {
        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        int geld = (int) propayManager.getAccount(benutzer.getBenutzerName()).getAmount();

        model.addAttribute("Betrag", geld);
        model.addAttribute("benutzer", benutzer);
        return "ProfilBearbeiten";
    }

    /**
     * Uebernimmt das Verarbeiten der Benutzereingaben auf dem Profil.
     *
     * @param model    Das model.
     * @param ben      Der aktuelle Benutzer.
     * @param aufladen Die Summe zum aufladen.
     * @param account  Der Account des Benutzers.
     * @return "Profil".
     */
    @PostMapping("/ProfilBearbeiten")
    public String profilBearbeitenSpeichern(Model model,
                                            @ModelAttribute Benutzer ben,
                                            int aufladen,
                                            Principal account) {
        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        Benutzer newBenutzer = benutzerManager.editBenutzer(benutzer, ben.getBenutzerEmail());
        benutzerManager.geldAufladen(newBenutzer, aufladen);
        model.addAttribute("benutzer", newBenutzer);
        return "redirect:/Profil";
    }
}
