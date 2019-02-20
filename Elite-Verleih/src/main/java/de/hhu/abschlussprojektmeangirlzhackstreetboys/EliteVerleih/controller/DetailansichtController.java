package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
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

    DataSync sync = new DataSync();

    @Autowired
    AusleiheManager ausleiheManager;

    @Autowired
    ArtikelManager artikelManager;

    @Autowired
    BenutzerManager benutzerManager;


    @GetMapping("/Detailansicht/{artikelId}")
    public String DetailansichtAnzeigen(@PathVariable Long artikelId, Model model, Long id){
        model.addAttribute("artikel", artikelManager.getArtikelById(artikelId));
        model.addAttribute("benutzer", benutzerManager.getBenutzerById(id));
        return "Detailansicht";
    }

    @GetMapping("/FehlendesGuthaben")
    public String fehlendesGuthaben(Model model, Long id){
        model.addAttribute("benutzer", benutzerManager.getBenutzerById(id));

        return "FehlendesGuthaben";
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

        double guthabenB = sync.getAccount(b.getBenutzerName()).getAmount();

        if(guthabenB < artikel.getArtikelKaution()){
            return "redirect:/FehlendesGuthaben?id=" + b.getBenutzerId();
        }else{
            ausleiheManager.erstelleAusleihe(b.getBenutzerId(),artikel.getArtikelId(),startDatum,endDatum);
        }



        return "redirect:/Uebersicht?id=" + b.getBenutzerId();
    }





}
