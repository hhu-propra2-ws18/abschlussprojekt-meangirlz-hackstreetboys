package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.AusleiheManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class DetailansichtController {

    @Autowired
    AusleiheManager ausleiheManager;

    @Autowired
    ArtikelManager artikelManager;

    @Autowired
    BenutzerManager benutzerManager;

    @Autowired
    ArtikelRepository arikelRepo;


    @GetMapping("/Detailansicht/{artikelId}")
    public String DetailansichtAnzeigen(@PathVariable Long artikelId, Model model, Long id){
        model.addAttribute("artikel", artikelManager.getArtikelById(artikelId));
        model.addAttribute("benutzer", benutzerManager.getBenutzerById(id));
        return "Detailansicht";
    }

    @PostMapping("/Detailansicht/{artikelId}")
    public String erstelleAusleihe(@RequestParam(required = false) String startDatumString,
                        @RequestParam(required = false) String endDatumString,
                        @PathVariable Long artikelId, Long id) {

        Date startDatum = new Date();
        Date endDatum = new Date();

        try {
            startDatum = new SimpleDateFormat( "yyyy-mm-dd" ).parse(startDatumString);
            endDatum = new SimpleDateFormat( "yyyy-mm-dd" ).parse(endDatumString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Benutzer b = benutzerManager.getBenutzerById(id);
        Artikel artikel = artikelManager.getArtikelById(artikelId);
        if (!ausleiheManager.isAusgeliehen(artikelId, startDatum, endDatum)) {
            return "redirect:/Ausgeliehen?id="+b.getBenutzerId();
        }
        Ausleihe aus = ausleiheManager.erstelleAusleihe(b.getBenutzerId(),artikel.getArtikelId(),startDatum,endDatum);
        artikel.getAusgeliehen().add(aus);
        arikelRepo.save(artikel);
        return "redirect:/Uebersicht?id=" + b.getBenutzerId();
    }

    @GetMapping("Ausgeliehen")
    public String ausgeliehenError (Long id, Model model) {
        model.addAttribute("benutzer", benutzerManager.getBenutzerById(id));
        return "Ausgeliehen-Error";
    }



}
