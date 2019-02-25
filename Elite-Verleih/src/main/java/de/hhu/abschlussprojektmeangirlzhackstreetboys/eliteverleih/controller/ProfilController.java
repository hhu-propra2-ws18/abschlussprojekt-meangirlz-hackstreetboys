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
public class ProfilController {
    @Autowired
    BenutzerManager benutzerManager;

    @Autowired
    AusleiheManager ausleiheManager;

    @Autowired
    ArtikelManager artikelManager;

    PropayManager propayManager = new PropayManager();

    /**
     * Kuemmert sich um das korrekte Anzeigen der Profilseite.
     *
     * @param model   Das zu uebergebende Model
     * @param account Principal des Benutzers
     * @return "Profil"
     */
    @GetMapping("/Profil")
    public String profilAnzeigen(Model model, Principal account) {

        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        model.addAttribute("benutzer", benutzer);

        List<Ausleihe> anfragen = benutzerManager.sucheEingehendeAnfragen(benutzer, Status.ANGEFRAGT);
        model.addAttribute("anfragen", anfragen);

        List<Ausleihe> zurueckerhaltene = benutzerManager.sucheEingehendeAnfragen(benutzer, Status.ABGEGEBEN);
        model.addAttribute("zurueckerhaltene", zurueckerhaltene);

        List<Ausleihe> konflikte = (benutzerManager.sucheEingehendeAnfragen(benutzer, Status.KONFLIKT));
        model.addAttribute("konflikte", konflikte);

        List<Ausleihe> bestaetigte = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.BESTAETIGT);
        model.addAttribute("bestaetigte", bestaetigte);

        List<Ausleihe> zurueckgegebene = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.ABGEGEBEN);
        model.addAttribute("zurueckgegebene", zurueckgegebene);

        List<Ausleihe> verliehenes = benutzerManager.sucheEingehendeAnfragen(benutzer, Status.BESTAETIGT);
        verliehenes.addAll(benutzerManager.sucheEingehendeAnfragen(benutzer, Status.AKTIV));
        verliehenes.addAll(benutzerManager.sucheEingehendeAnfragen(benutzer, Status.KONFLIKT));
        model.addAttribute("verliehenes", verliehenes);

        List<Ausleihe> erfolgreichZurueckgegeben = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.BEENDET);
        model.addAttribute("erfolgreichZurueckgegebene", erfolgreichZurueckgegeben);

        List<Ausleihe> wartendeAnfragen = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.ANGEFRAGT);
        model.addAttribute("wartendeAnfragen", wartendeAnfragen);

        int geld = (int) propayManager.getAccount(benutzer.getBenutzerName()).getAmount();
        model.addAttribute("Betrag", geld);

        List<Ausleihe> abgelehnteAnfragen = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.ABGELEHNT);
        model.addAttribute("abgelehnteAnfragen", abgelehnteAnfragen);


        List<Ausleihe> ausgehendeKonflikte = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.KONFLIKT);
        model.addAttribute("ausgehendeKonflikte", ausgehendeKonflikte);

        return "Profil";
    }

    /**
     * Uebernimmt das Verarbeiten der Buttons auf dem Profil.
     *
     * @param model     Das zu uebergebende Model
     * @param name      Der name des Buttons
     * @param anfrage   Das Ausleih Objekt
     * @param ausleihId Die Id der Ausleihe
     * @param account   Der account des Benutzers
     * @return "Profil"
     */
    @PostMapping("/Profil")
    public String profilAnzeigen(Model model, @RequestParam(value = "submitButton") String name,
                                 @ModelAttribute Ausleihe anfrage, Long ausleihId, Principal account) {
        if (name.equals("Problem")) {
            return "redirect:/Support";
        }
        if (name.equals("Bestaetigen")) {
            ausleiheManager.bestaetigeAusleihe(ausleihId);
            return "redirect:/Profil";
        } else if (name.equals("Ablehnen")) {
            ausleiheManager.bearbeiteAusleihe(ausleihId, Status.ABGELEHNT);
            return "redirect:/Profil";
        } else if (name.equals("Zurueckgeben")) {
            ausleiheManager.zurueckGeben(ausleihId);
            return "redirect:/Profil";
        } else if (name.equals("Akzeptieren")) {
            ausleiheManager.rueckgabeAkzeptieren(ausleihId);
            return "redirect:/Profil";
        } else if (name.equals("Entfernen")) {
            ausleiheManager.loescheAusleihe(ausleihId);
            return "redirect:/Profil";
        } else if (name.equals("Zurueckziehen")) {
            ausleiheManager.loescheAusleihe(ausleihId);
            return "redirect:/Profil";
        } else if (name.equals("Konflikt")) {
            ausleiheManager.bearbeiteAusleihe(ausleihId, Status.KONFLIKT);
            return "redirect:/Profil";
        } else if (name.equals("Geloest")) {
            ausleiheManager.bearbeiteAusleihe(ausleihId, Status.BEENDET);
            return "redirect:/Profil";
        }
        return "redirect:/Profil";
    }
}
