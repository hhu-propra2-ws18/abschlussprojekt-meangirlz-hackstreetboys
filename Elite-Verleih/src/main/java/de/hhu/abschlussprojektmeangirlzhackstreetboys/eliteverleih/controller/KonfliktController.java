package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.AusleiheManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.PropayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class KonfliktController {

    PropayManager sync = new PropayManager();

    @Autowired
    BenutzerManager benutzerManager;

    @Autowired
    AusleiheManager ausleiheManager;

    @Autowired
    ArtikelManager artikelManager;

    @GetMapping("/Konfliktloesung")
    public String konfliktloesungAnzeigen(Model model, Principal account) {
        List<Ausleihe> ausleihe = ausleiheManager.getAllAusleihe();
        model.addAttribute("konflikt", ausleiheManager.getKonflike(ausleihe));
        return "Konfliktloesung";
    }

    @PostMapping("/Konfliktloesung")
    public String konfliktLoesen(@RequestParam(value = "submitButton") String name,
                                 @ModelAttribute Ausleihe anfrage, Long ausleihId) {

        Ausleihe ausleihe = ausleiheManager.getAusleiheById(ausleihId);
        Benutzer benutzer = ausleihe.getBenutzer();

        if (name.equals("Buchung Verleihender")) {
            sync.getAccount(benutzer.getBenutzerName());
            sync.kautionEinziehen(ausleihe.getBenutzer().getBenutzerName(), ausleihe.getReservationsId());
        }

        if (name.equals("Buchung Ausleihender")) {
            sync.getAccount(benutzer.getBenutzerName());
            sync.kautionFreigeben(ausleihe.getBenutzer().getBenutzerName(), ausleihe.getReservationsId());
        }

        if (name.equals("Konflikt beenden")) {
            ausleiheManager.bearbeiteAusleihe(ausleihId, Status.BEENDET);
        }

        return "redirect:/Konfliktloesung";
    }
}
