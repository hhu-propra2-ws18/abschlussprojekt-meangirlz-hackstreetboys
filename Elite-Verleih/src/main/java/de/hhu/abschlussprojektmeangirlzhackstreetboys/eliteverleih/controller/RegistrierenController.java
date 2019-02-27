package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrierenController {

    @Autowired
    BenutzerManager benutzerManager;

    /**
     * Zeigt die Registrierung.
     *
     * @param model Datencontainer fuer die View.
     * @return "Registrierung"
     */
    @GetMapping("/registrieren")
    public String registrierenAnzeigen(Model model) {
        model.addAttribute("benutzer", new Benutzer());
        return "Registrierung";
    }

    /**
     * Ueberprueft ob die BindingResult ein Error wirft. Wenn kein Error geworfen wurde wird ein Benutzer angelegt.
     * Falls es Probleme beim anlegen gibt, wie zum Beispiel der Benutzername ist schon vorhanden, wird das
     * Errorflag gesetzt.
     *
     * @param benutzer Ein befuellter Benutzer der die eingaben gespeichert hat
     * @param result   Die BindingResult
     * @param request  Gibt Daten ueber den Security Zustand an
     * @return  Ein Redirect auf die Uebersichtsseite falls es keinen Fehler gab
     */
    @PostMapping("/registrieren")
    public String registereBenutzer(@ModelAttribute @Valid Benutzer benutzer,
                                    BindingResult result,
                                    HttpServletRequest request) throws ServletException {
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
