package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AnmeldeController {

    @Autowired
    BenutzerManager benutzerManager;

    @GetMapping("/login")
    public String AnmeldungAnzeigen(Model model, Integer login) {
        model.addAttribute("benutzer", new Benutzer());
        return "Anmeldung";
    }
    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @RequestParam(required = false) String username,
                        @RequestParam(required = false) String password){
        if(!benutzerManager.nameSchonVorhanden(username)){
            return "redirect:/login?error";
        }
        try {
            request.login(username,password);
        }
        catch(ServletException e){
            System.err.println(e.getMessage());
            return "redirect:/login?error";
        }
        return "redirect:/Uebersicht?id=1";
    }
}
