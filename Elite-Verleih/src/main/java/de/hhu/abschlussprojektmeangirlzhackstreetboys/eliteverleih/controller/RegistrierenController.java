package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrierenController {

    @Autowired
    BenutzerManager benutzerManager;

    @GetMapping("/registrieren")
    public String registrierenAnzeigen (Model model){
        model.addAttribute("benutzer", new Benutzer());
        return "Registrierung";
    }
    @PostMapping("/registrieren")
    public String registereBenutzer
            (@ModelAttribute @Valid Benutzer benutzer,
             BindingResult result, HttpServletRequest request) throws ServletException {
        Benutzer registered = new Benutzer();

        if (!result.hasErrors()) {
            benutzer.setBenutzerPasswort(benutzer.getBenutzerPasswort());
            benutzer.setBenutzerRolle("ROLE_USER");
            registered = benutzerManager.erstelleBenutzer(benutzer);
        }
        if (registered == null) {
            result.rejectValue("benutzerName", "message.regError");
            return "redirect:/registrieren?error";
        }
        request.login(registered.getBenutzerName(), registered.getBenutzerPasswort());
        return "redirect:/Uebersicht";
    }
}
