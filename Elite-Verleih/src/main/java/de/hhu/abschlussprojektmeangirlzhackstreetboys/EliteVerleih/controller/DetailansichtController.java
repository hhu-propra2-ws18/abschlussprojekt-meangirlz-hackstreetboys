package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.AusleiheRepository;
//import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DetailansichtController {

    @Autowired
    ArtikelRepository artikelRepo;

    @Autowired
    AusleiheRepository ausleiheRepo;

    @Autowired
    BenutzerManager benutzerManager;


    @GetMapping("/Detailansicht/{artikelId}")
    public String DetailansichtAnzeigen(@PathVariable long artikelId, Model model, Long id){
        Benutzer benutzer = benutzerManager.getBenutzerById(id);
        model.addAttribute("artikel", artikelRepo.findArtikelByArtikelId(artikelId));
        model.addAttribute("benutzer", benutzer);
        return "Detailansicht";
    }






}

