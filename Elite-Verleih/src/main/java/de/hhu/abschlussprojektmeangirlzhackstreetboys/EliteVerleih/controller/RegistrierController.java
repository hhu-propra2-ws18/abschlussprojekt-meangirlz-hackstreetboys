package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrierController {

    @Autowired
    BenutzerManager benutzerManager;

    @GetMapping("/registrieren")
    public String registrierungAnzeigen(Model model){
        model.addAttribute("benutzer",new Benutzer());
        return "Registrierung";
    }

    @PostMapping("/registrieren")
    public String login(HttpServletRequest request,
                        @ModelAttribute Benutzer benutzer) throws ServletException {
        if (benutzer == null) {
            return "redirect:/";
        }
        benutzer = benutzerManager.erstelleBenutzer(benutzer);

        request.login(benutzer.getBenutzerName(),"password");
        return "redirect:/Uebersicht?id=1";
    }
}
