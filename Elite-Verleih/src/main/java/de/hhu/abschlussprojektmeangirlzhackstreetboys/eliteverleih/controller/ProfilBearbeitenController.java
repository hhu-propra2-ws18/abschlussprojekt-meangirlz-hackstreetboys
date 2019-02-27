package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
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

    BenutzerManager benutzerManager;


    PropayManager propayManager;

    @Autowired
    public ProfilBearbeitenController(BenutzerManager benutzerManager,
                            PropayManager propayManager){
        this.benutzerManager = benutzerManager;
        this.propayManager = propayManager;
    }

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

        AccountDto acc = propayManager.getAccount(benutzer.getBenutzerName());
        if (acc == null) {
            model.addAttribute("Betrag", "Propay nicht erreichbar xx,xx");
        }
        else{
            double betrag = acc.getAmount();
            model.addAttribute("Betrag", betrag);
        }

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
        if (!benutzerManager.geldAufladen(benutzer, aufladen)){
            return "ErrorPropay";
        }

        benutzerManager.bearbeiteBenutzer(benutzer.getBenutzerId(), ben);
        model.addAttribute("benutzer", benutzer);
        return "redirect:/Profil";
    }
}
