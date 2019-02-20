package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Status;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.AusleiheManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class KonfliktController {

    DataSync sync = new DataSync();

    @Autowired
    BenutzerManager benutzerManager;

    @Autowired
    AusleiheManager ausleiheManager;

    @Autowired
    ArtikelManager artikelManager;

    @GetMapping("/Konfliktloesung")
    public String ProfilAnzeigen(Long id, Model model){
        if(id != 1) {
            return "redirect:/";
        }

        List<Ausleihe> ausleihe = ausleiheManager.getAllAusleihe();
        model.addAttribute("konflikt", ausleiheManager.getKonflike(ausleihe));
        return "Konfliktloesung";
    }

    @PostMapping("/Konfliktloesung")
    public String profilAnzeigen(@RequestParam(value= "submitButton") String name,
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
            ausleiheManager.bearbeiteAusleihe(ausleihId,Status.BEENDET);
        }

        return "redirect:/Konfliktloesung?id=1";

    }
}
