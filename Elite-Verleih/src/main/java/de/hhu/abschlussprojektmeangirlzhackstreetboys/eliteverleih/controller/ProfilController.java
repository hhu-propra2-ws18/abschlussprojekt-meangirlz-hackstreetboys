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

    PropayManager sync = new PropayManager();

    @GetMapping("/Profil")
    public String profilAnzeigen(Model model, Principal account) {

        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        model.addAttribute("benutzer", benutzer);
        List<Ausleihe> wartend = benutzerManager.sucheAnfragen(benutzer, Status.ANGEFRAGT);
        List<Ausleihe> zurueckerhaltene = benutzerManager.sucheAnfragen(benutzer, Status.ABGEGEBEN);
        List<Ausleihe> konflikte = (benutzerManager.sucheAnfragen(benutzer, Status.KONFLIKT));
        List<Ausleihe> bestaetigte = benutzerManager.sucheEigeneAnfragen(benutzer, Status.BESTAETIGT);
        List<Ausleihe> zurueckgegebene = benutzerManager.sucheEigeneAnfragen(benutzer, Status.ABGEGEBEN);
        List<Ausleihe> verliehenes = benutzerManager.sucheAnfragen(benutzer, Status.BESTAETIGT);
        verliehenes.addAll(benutzerManager.sucheAnfragen(benutzer, Status.AKTIV));
        verliehenes.addAll(benutzerManager.sucheAnfragen(benutzer, Status.KONFLIKT));
        List<Ausleihe> erfolgreichZurueckgegeben = benutzerManager.sucheEigeneAnfragen(benutzer, Status.BEENDET);
        List<Ausleihe> eigeneAnfragen = benutzerManager.sucheEigeneAnfragen(benutzer, Status.ANGEFRAGT);
        int geld = (int) sync.getAccount(benutzer.getBenutzerName()).getAmount();

        model.addAttribute("wartendeAnfragen", eigeneAnfragen);
        model.addAttribute("erfolgreichZurueckgegebene", erfolgreichZurueckgegeben);
        model.addAttribute("verliehenes", verliehenes);
        model.addAttribute("zurueckerhaltene", zurueckerhaltene);
        model.addAttribute("zurueckgegebene", zurueckgegebene);
        model.addAttribute("bestaetigte", bestaetigte);
        model.addAttribute("anfragen", wartend);
        model.addAttribute("konflikte", konflikte);
        model.addAttribute("Betrag", geld);

        return "Profil";
    }

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
            ausleiheManager.bearbeiteAusleihe(ausleihId, Status.BEENDET);
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
        } else {
            return "redirect:/Uebersicht";
        }
    }


}

