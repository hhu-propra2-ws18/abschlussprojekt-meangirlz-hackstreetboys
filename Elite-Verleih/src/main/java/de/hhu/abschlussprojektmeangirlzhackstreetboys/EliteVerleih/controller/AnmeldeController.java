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
import javax.servlet.http.HttpServletResponse;

@Controller
public class AnmeldeController {

    @Autowired
    BenutzerManager benutzerManager;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @GetMapping("/login")
    public String AnmeldungAnzeigen(Model model, Integer login) {
        if (login != null) {
            model.addAttribute("istFalsch", login);
        } else {
            model.addAttribute("istFalsch", 0);
        }
        model.addAttribute("benutzer", new Benutzer());
        return "Anmeldung";
    }

    /*
    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, BindingResult result,
                        @RequestParam(required = false) String username,
                        @RequestParam(required = false) String password) {

        System.err.println("Panik!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Benutzer benutzer = benutzerManager.findBenutzerByName(username);
        if (benutzer == null) {
            return "redirect:/";
        }
        try {
            request.login(username, password);
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            if (savedRequest == null) {
                return "redirect:/";
            }

        } catch (Exception e) {
            result.rejectValue(null, "authentication.failed");
            return "login";
        }

        return "redirect:/Uebersicht?id=" + benutzer.getBenutzerId();
    } */
    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @RequestParam(required = false) String username,
                        @RequestParam(required = false) String password) throws ServletException {

        System.err.println("Panik!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + username);
        request.login(username,password);
        return "redirect:/Uebersicht?id=1";
    }

    @GetMapping("/Logout")
    public String logoutAnzeigen(Model model, Integer login) {
        return "logout";
    }

    @PostMapping("/Logout")
    public String logout(HttpServletRequest request,Model model, Integer login) throws ServletException {

        request.logout();
        return "redirect:/logout";
    }
}
