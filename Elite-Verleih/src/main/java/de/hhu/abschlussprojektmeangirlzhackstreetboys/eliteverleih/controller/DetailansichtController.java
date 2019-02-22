package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.AusleiheManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.PropayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Controller
public class DetailansichtController {

    PropayManager sync = new PropayManager();

    @Autowired
    AusleiheManager ausleiheManager;

    @Autowired
    ArtikelManager artikelManager;

    @Autowired
    BenutzerManager benutzerManager;


    @GetMapping("/Detailansicht/{artikelId}")
    public String detailansichtAnzeigen(@PathVariable Long artikelId, Model model, Principal account) {
        model.addAttribute("artikel", artikelManager.getArtikelById(artikelId));
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));
        return "Detailansicht";
    }

    @GetMapping("/FehlendesGuthaben")
    public String fehlendesGuthaben(Model model, Principal account) {
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));

        return "FehlendesGuthaben";
    }

    @PostMapping("/Detailansicht/{artikelId}")
    public String erstelleAusleihe(@RequestParam(required = false) String startDatumString,
                                   @RequestParam(required = false) String endDatumString,
                                   @PathVariable Long artikelId, Principal account) {


        Calendar calStartDatum = new GregorianCalendar();
        Calendar calEndDatum = new GregorianCalendar();

        try {
            Date startDatum = new SimpleDateFormat("yyyy-MM-dd").parse(startDatumString);
            calStartDatum.setTime(startDatum);
            Date endDatum = new SimpleDateFormat("yyyy-MM-dd").parse(endDatumString);
            calEndDatum.setTime(endDatum);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Benutzer b = benutzerManager.findBenutzerByName(account.getName());
        Artikel artikel = artikelManager.getArtikelById(artikelId);
        if (ausleiheManager.isAusgeliehen(artikelId, calStartDatum, calEndDatum)) {
            return "redirect:/Ausgeliehen";
        }

        double guthabenB = sync.getAccount(b.getBenutzerName()).getAmount();

        if (guthabenB < artikel.getArtikelKaution()) {
            return "redirect:/FehlendesGuthaben";
        } else {
            ausleiheManager.erstelleAusleihe(b.getBenutzerId(), artikel.getArtikelId(), calStartDatum, calEndDatum);
        }


        return "redirect:/Uebersicht";
    }

    @GetMapping("Ausgeliehen")
    public String ausgeliehenError(Model model, Principal account) {
        model.addAttribute("benutzer", benutzerManager.findBenutzerByName(account.getName()));
        return "Ausgeliehen-Error";
    }


}
