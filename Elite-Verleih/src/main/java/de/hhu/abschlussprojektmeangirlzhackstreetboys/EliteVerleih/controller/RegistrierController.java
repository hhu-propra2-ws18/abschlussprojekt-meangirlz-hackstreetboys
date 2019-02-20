package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;

@Controller
public class RegistrierController {

    @Autowired
    BenutzerManager benutzerManager;

    @GetMapping("/registrieren")
    public String showRegistrationForm (Model model){
        model.addAttribute("benutzer", new Benutzer());
        return "Registrierung";
    }

    @RequestMapping(value = "/registrieren", method = RequestMethod.POST)
    public String registerBenutzer
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
