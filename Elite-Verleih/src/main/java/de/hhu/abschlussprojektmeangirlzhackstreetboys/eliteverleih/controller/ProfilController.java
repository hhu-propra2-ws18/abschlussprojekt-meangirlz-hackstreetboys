package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
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

    @Autowired
    PropayManager propayManager;

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
        model.addAttribute("anfragen", wartend);

        List<Ausleihe> zurueckerhaltene = benutzerManager.sucheEingehendeAnfragen(benutzer, Status.ABGEGEBEN);
        model.addAttribute("zurueckerhaltene", zurueckerhaltene);

        List<Ausleihe> konflikte = (benutzerManager.sucheEingehendeAnfragen(benutzer, Status.KONFLIKT));
        model.addAttribute("konflikte", konflikte);

        List<Ausleihe> bestaetigte = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.BESTAETIGT);
        model.addAttribute("bestaetigte", bestaetigte);

        List<Ausleihe> zurueckgegebene = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.ABGEGEBEN);
        model.addAttribute("zurueckgegebene", zurueckgegebene);

        List<Ausleihe> verliehene = benutzerManager.sucheEingehendeAnfragen(benutzer, Status.BESTAETIGT);
        model.addAttribute("verliehene", verliehene);

        verliehene.addAll(benutzerManager.sucheEingehendeAnfragen(benutzer, Status.AKTIV));

        verliehene.addAll(benutzerManager.sucheEingehendeAnfragen(benutzer, Status.KONFLIKT));

        List<Ausleihe> erfolgreichZurueckgegeben = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.BEENDET);
        model.addAttribute("erfolgreichZurueckgegebene", erfolgreichZurueckgegeben);

        List<Ausleihe> eigeneAnfragen = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.ANGEFRAGT);
        model.addAttribute("wartendeAnfragen", eigeneAnfragen);

        AccountDto acc = propayManager.getAccount(benutzer.getBenutzerName());
        if (acc == null) {
            model.addAttribute("Betrag", "Propay nicht erreichbar xxxx");
        } else {
            double betrag = acc.getAmount();
            model.addAttribute("Betrag", betrag);
        }

        List<Ausleihe> abgelehnteAnfragen = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.ABGELEHNT);
        model.addAttribute("abgelehnteAnfragen", abgelehnteAnfragen);

        List<Ausleihe> ausgehendeKonflikte = benutzerManager.sucheAusgehendeAnfragen(benutzer, Status.KONFLIKT);
        model.addAttribute("ausgehendeKonflikte", ausgehendeKonflikte);

        Calendar aktuellesDatum = new GregorianCalendar();

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
            if (!ausleiheManager.bestaetigeAusleihe(ausleihId)) {
                return "ErrorPropay";
            }
            return "redirect:/Profil";
        } else if (name.equals("Ablehnen")) {
            ausleiheManager.bearbeiteAusleihe(ausleihId, Status.ABGELEHNT);
            return "redirect:/Profil";
        } else if (name.equals("Zurueckgeben")) {
            int code = ausleiheManager.zurueckGeben(ausleihId);
            if (code < 500 && code > 200) {
                return "redirect:/Profil/" + "?error";
            } else if (code > 500) {
                return "ErrorPropay";
            }
            return "redirect:/Profil";
        } else if (name.equals("Akzeptieren")) {
            if (!ausleiheManager.rueckgabeAkzeptieren(ausleihId)) {
                return "ErrorPropay";
            }
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
