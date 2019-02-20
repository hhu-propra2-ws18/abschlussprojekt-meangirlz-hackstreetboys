package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SupportController {
    @Autowired
    BenutzerManager benutzerManager;

    @GetMapping("/Support")
    public String DetailansichtAnzeigen(Long id, Model model){
        if(id == null) {
            return "redirect:/";
        }
        Benutzer benutzer = benutzerManager.getBenutzerById(id);
        model.addAttribute("benutzer",benutzer);

        return "Support";
    }
}
