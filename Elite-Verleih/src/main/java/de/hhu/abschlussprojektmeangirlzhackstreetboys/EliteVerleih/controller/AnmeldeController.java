package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnmeldeController {

    @Autowired
    DataManager dataManager;

    @GetMapping("/")
    public String AnmeldungAnzeigen(){
        return "Anmeldung";
    }

    @PostMapping("/")
    public String login(@RequestParam(value = "submitButton") String name,
                        @RequestParam(required = false) String registBenutzername,
                        @RequestParam(required = false) String registEmail,
                        @RequestParam(required = false) String loginBenutzername,
                        Model model){

        System.err.println(name);
        Benutzer benutzer = new Benutzer();
        if (name.equals("Registrieren")){
            benutzer = dataManager.erstelleBenutzer(registBenutzername,registEmail);
        }

        return "redirect:/Uebersicht?id=" + benutzer.getBenutzerId();
    }
}
