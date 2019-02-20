package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class SupportController {
    @Autowired
    BenutzerManager benutzerManager;

    @GetMapping("/Support")
    public String detailansichtAnzeigen(Model model, Principal account) {
        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        model.addAttribute("benutzer", benutzer);

        return "Support";
    }
}
