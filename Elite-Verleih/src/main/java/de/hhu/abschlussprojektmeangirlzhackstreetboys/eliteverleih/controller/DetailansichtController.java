package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
public class DetailansichtController {

    PropayManager propayManager = new PropayManager();

    @Autowired
    AusleiheManager ausleiheManager;

    @Autowired
    ArtikelManager artikelManager;

    @Autowired
    BenutzerManager benutzerManager;

    /**
     * Zeigt die Detailansicht.
     *
     * @param artikelId Die ArtikelId des anzuzeigenden Artikels
     * @param model     Das zu uebergebende Model
     * @param account   Der account des Benutzers
     * @return Die Detailansicht View
     */
    @GetMapping("/Detailansicht/{artikelId}")
    public String detailansichtAnzeigen(@PathVariable Long artikelId, Model model, Principal account) {
        Artikel artikel = artikelManager.getArtikelById(artikelId);
        List<Ausleihe> bestaetigteAusleihen = new ArrayList<>();
        for (Ausleihe ausleihe : artikel.getAusgeliehen()) {
            if (ausleihe.getAusleihStatus() == Status.BESTAETIGT) {
                bestaetigteAusleihen.add(ausleihe);
            }
        }
        model.addAttribute("artikel", artikel);
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));
        model.addAttribute("bestaetigteAusleihen", bestaetigteAusleihen);
        return "Detailansicht";
    }

    /**
     * Geht auf die Error Seite wenn zuwenig Guthaben da ist.
     *
     * @param model   Das zu uebergebende Model
     * @param account Der account des Benutzers
     * @return Gibt einen Error da zuwenig Guthaben aus dem Konto ist
     */
    @GetMapping("/FehlendesGuthaben")
    public String fehlendesGuthaben(Model model, Principal account) {
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));

        return "FehlendesGuthaben";
    }

    /**
     * Erstellt eine Ausleihe mit den angegebenen Daten.
     *
     * @param startDatumString Das Startdatum der Ausleihe
     * @param endDatumString   Das Enddatum der Ausleihe
     * @param artikelId        Die ArtikelId des Artikels
     * @param account          Der account des Benutzers
     * @return Fuehrt zurueck aus die Uebersicht
     */
    @PostMapping("/Detailansicht/{artikelId}")
    public String erstelleAusleihe(@RequestParam(required = false) String startDatumString,
                                   @RequestParam(required = false) String endDatumString,
                                   @PathVariable Long artikelId, Principal account) {

        String[] enddatum = endDatumString.split("-");
        String[] startdatum = startDatumString.split("-");
        Calendar calStartDatum = new GregorianCalendar(Integer.parseInt(startdatum[0]),
            Integer.parseInt(startdatum[1])-1,
            Integer.parseInt(startdatum[2]));
        Calendar calEndDatum = new GregorianCalendar(Integer.parseInt(enddatum[0]),
            Integer.parseInt(enddatum[1])-1,
            Integer.parseInt(enddatum[2]));

        if (calStartDatum.after(calEndDatum)) {
            return "redirect:/Detailansicht/" + artikelId + "?error=falseDate";
        }

        Benutzer b = benutzerManager.findBenutzerByName(account.getName());
        Artikel artikel = artikelManager.getArtikelById(artikelId);
        if (ausleiheManager.isAusgeliehen(artikelId, calStartDatum, calEndDatum)) {
            return "redirect:/Ausgeliehen";
        }

        double guthabenB = propayManager.getAccount(b.getBenutzerName()).getAmount();

        if (guthabenB < artikel.getArtikelKaution()) {
            return "redirect:/FehlendesGuthaben";
        } else {
            ausleiheManager.erstelleAusleihe(b.getBenutzerId(), artikel.getArtikelId(), calStartDatum, calEndDatum);
        }


        return "redirect:/Uebersicht";
    }

    /**
     * Zeigt die Error Seite wenn es einen Fehler bei der Ausleihe gab.
     *
     * @param model   Das zu uebergebende Model
     * @param account Der account des Benutzers
     * @return
     */
    @GetMapping("Ausgeliehen")
    public String ausgeliehenError(Model model, Principal account) {
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));
        return "Ausgeliehen-Error";
    }

    /**
     * Prueft, ob Ausleihen bestehen und loescht falls nicht.
     *
     * @param artikelId Eindeutige ID des Artikels.
     * @param account   Aktive Benutzer.
     * @return Uebersicht beim Erfolgreichen loeschen und Error falls nicht.
     */
    @RequestMapping("/Kaufen/{artikelId}")
    public String artikelLoeschen(@PathVariable long artikelId,
                                  Principal account) {
        /* TODO: ProPay etc. */
        Benutzer b = benutzerManager.findBenutzerByName(account.getName());
        Artikel a = artikelManager.getArtikelById(artikelId);
        double guthaben = sync.getAccount(b.getBenutzerName()).getAmount();
        if(guthaben>=a.getArtikelPreis()) {
            sync.ueberweisen(account.getName(), artikelManager.getArtikelById(artikelId).getBenutzer().getBenutzerName(),
            artikelManager.getArtikelById(artikelId).getArtikelPreis());
            artikelManager.loescheArtikel(artikelId);
            return "redirect:/Uebersicht";
        }
        return "redirect:/FehlendesGuthaben";
    }
}



