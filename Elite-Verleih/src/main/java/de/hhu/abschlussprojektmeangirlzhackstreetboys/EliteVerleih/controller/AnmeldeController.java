package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.DataManager;
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
    DataManager dataManager;

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
                        @RequestParam(required = false) String loginBenutzername,
                        @ModelAttribute Benutzer benutzer){

        if (name.equals("Registrieren")){
            benutzer = dataManager.erstelleBenutzer(benutzer);

            if (benutzer == null){
                return "redirect:/?login=1";
            }
        }
        if (name.equals("Anmelden")){

            benutzer = dataManager.findBenutzerByName(loginBenutzername);
            if (benutzer == null){
                return "redirect:/?login=1";
            }
        }

        return "redirect:/Uebersicht?id=" + benutzer.getBenutzerId();
    }
}
