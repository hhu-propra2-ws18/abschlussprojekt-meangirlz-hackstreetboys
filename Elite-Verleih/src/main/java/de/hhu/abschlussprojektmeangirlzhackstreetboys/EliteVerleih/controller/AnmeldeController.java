package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnmeldeController {

    @Autowired
    BenutzerManager benutzerManager;

    @GetMapping("/")
    public String AnmeldungAnzeigen(Model model, Integer login){
        if (login != null){
            model.addAttribute("istFalsch",login);
        }
        else{
            model.addAttribute("istFalsch",0);
        }
        model.addAttribute("benutzer",new Benutzer());
        return "Anmeldung";
    }

    @PostMapping("/")
    public String login(@RequestParam(value = "submitButton") String name,
                        @RequestParam(required = false) String registBenutzername,
                        @RequestParam(required = false) String registEmail,
                        @RequestParam(required = false) String loginBenutzername,
                        @ModelAttribute Benutzer benutzer){

        System.err.println(name);
        if (name.equals("Registrieren")){
            benutzer = benutzerManager.erstelleBenutzer(benutzer);

            if (benutzer == null){
                return "redirect:/?login=1";
            }
        }
        if (name.equals("Anmelden")){

            benutzer = benutzerManager.findBenutzerByName(loginBenutzername);
            if (benutzer == null){
                return "redirect:/?login=1";
            }
        }

        return "redirect:/Uebersicht?id=" + benutzer.getBenutzerId();
    }
}
