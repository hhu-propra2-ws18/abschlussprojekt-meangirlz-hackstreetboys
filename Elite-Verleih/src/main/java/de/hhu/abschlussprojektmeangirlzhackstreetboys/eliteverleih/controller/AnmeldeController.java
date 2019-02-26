package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String anmeldungAnzeigen(Model model) {
        model.addAttribute("benutzer", new Benutzer());
        return "Anmeldung";
    }

    /**
     * Sorgt dafuer das ein User eingeloggt wird. Falls es den User gibt oder das Passwort falsch ist wird er
     * auf die "/login?error" weitergeleitet. Zusätlich wird der Support Account abgefangen und auf
     * die entsprechende Konfliktseite weiter geleitet
     *
     * @param request  fragt die Daten der HttpServletRequest ab
     * @param username Eingabefeld fuer den Username wird abgefragt
     * @param password Eingabefeld fuer den password wird abgefragt
     * @return leitet auf die Uebersicht weiter
     */
    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @RequestParam(required = false) String username,
                        @RequestParam(required = false) String password) {
        if (!benutzerManager.nameSchonVorhanden(username)) {
            return "redirect:/login?error";
        }
        try {
            request.login(username, password);
        } catch (ServletException e) {
            System.err.println(e.getMessage());
            return "redirect:/login?error";
        }
        if (request.isUserInRole("ROLE_SUPPORT")) {
            return "redirect:/Konfliktloesung";
        }
        return "redirect:/Uebersicht";
    }
}
