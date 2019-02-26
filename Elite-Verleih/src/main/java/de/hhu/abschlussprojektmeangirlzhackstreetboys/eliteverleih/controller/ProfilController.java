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
import java.util.*;

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
        List<Ausleihe> wartend = benutzerManager.sucheEingehendeAnfragen(benutzer, Status.ANGEFRAGT);
        List<Ausleihe> zurueckerhaltene = benutzerManager.sucheEingehendeAnfragen(benutzer, Status.ABGEGEBEN);
        List<Ausleihe> konflikte = (benutzerManager.sucheEingehendeAnfragen(benutzer, Status.KONFLIKT));
        List<Ausleihe> bestaetigte = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.BESTAETIGT);
        List<Ausleihe> zurueckgegebene = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.ABGEGEBEN);
        List<Ausleihe> verliehene = benutzerManager.sucheEingehendeAnfragen(benutzer, Status.BESTAETIGT);
        verliehene.addAll(benutzerManager.sucheEingehendeAnfragen(benutzer, Status.AKTIV));
        verliehene.addAll(benutzerManager.sucheEingehendeAnfragen(benutzer, Status.KONFLIKT));
        List<Ausleihe> erfolgreichZurueckgegeben = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.BEENDET);
        List<Ausleihe> eigeneAnfragen = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.ANGEFRAGT);
        int geld = (int) propayManager.getAccount(benutzer.getBenutzerName()).getAmount();
        List<Ausleihe> abgelehnteAnfragen = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.ABGELEHNT);
        List<Ausleihe> ausgehendeKonflikte = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.KONFLIKT);

        model.addAttribute("wartendeAnfragen", eigeneAnfragen);
        model.addAttribute("erfolgreichZurueckgegebene", erfolgreichZurueckgegeben);
        model.addAttribute("verliehene", verliehene);
        model.addAttribute("zurueckerhaltene", zurueckerhaltene);
        model.addAttribute("zurueckgegebene", zurueckgegebene);
        model.addAttribute("bestaetigte", bestaetigte);
        model.addAttribute("anfragen", wartend);
        model.addAttribute("konflikte", konflikte);
        model.addAttribute("Betrag", geld);
        model.addAttribute("abgelehnteAnfragen", abgelehnteAnfragen);
        model.addAttribute("ausgehendeKonflikte", ausgehendeKonflikte);

        Calendar aktuellesDatum = new GregorianCalendar();
        aktuellesDatum.setTime(new Date());
        aktuellesDatum.set(2019,Calendar.FEBRUARY,25);
        System.err.println("TEST");
        System.err.println(aktuellesDatum.get(Calendar.DATE));
        System.err.println(aktuellesDatum.get(Calendar.MONTH) + 1);
        System.err.println(aktuellesDatum.get(Calendar.YEAR));

        model.addAttribute("aktuellesDatum", aktuellesDatum);

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
            ausleiheManager.rueckgabeAkzeptieren(ausleihId);
            return "redirect:/Profil";
        }
        return "redirect:/Profil";
    }
}
