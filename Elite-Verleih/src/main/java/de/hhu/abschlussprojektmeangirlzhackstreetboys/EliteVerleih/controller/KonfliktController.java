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
        Benutzer benutzer;

        System.out.println("Test:"+ ausleihId);

        if (name.equals("Buchung Verleihender")) {
            System.out.println("Test1 ");
            benutzer = ausleihe.getArtikel().getBenutzer();
            // Kaution wird an Verleihenden gezahlt
        }

        if (name.equals("Buchung Ausleihender")) {
            benutzer = ausleihe.getBenutzer();
            // Kaution wird an Ausleihenden zurückgezahlt
        }

        if (name.equals("Konflikt beenden")) {
            ausleiheManager.setzeSatusAusleihe(ausleihe, "BEENDET");
        }


        return "redirect:/Konfliktloesung?id=1";

    }
}
