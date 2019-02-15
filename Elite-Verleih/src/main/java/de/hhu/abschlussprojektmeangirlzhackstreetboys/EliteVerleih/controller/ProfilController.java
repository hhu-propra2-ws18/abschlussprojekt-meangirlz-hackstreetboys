package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Status;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.AusleiheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfilController {
	@Autowired
    BenutzerManager benutzerManager;

	@Autowired
	AusleiheManager ausleiheManager;


    @GetMapping("/Profil")
    public String ProfilAnzeigen(Long id, Model model){
    	if(id == null) {
    		return "redirect:/";
    	}
    	
    	Benutzer benutzer = benutzerManager.getBenutzerById(id);
    	model.addAttribute("benutzer",benutzer);
    	List<Ausleihe> wartend = benutzerManager.sucheAnfragen(benutzer);
		model.addAttribute("anfragen", wartend);

        return "Profil";
    }
    
    @PostMapping("/Profil")
    public String profilAnzeigen(Long id, @RequestParam(value= "submitButton") String name,
    		 Model model, Ausleihe ausleihe) {
    	if (name.equals("Problem")) {
    		return "redirect:/Support?id=" + id;
    	}

		if (name.equals("Bestaetigen")) {
			Benutzer benutzer = benutzerManager.getBenutzerById(id);
			ausleiheManager.bestaetigeAusleihe(ausleihe);
            //model.addAttribute("ausleihe", ausleihe);
            System.out.println("TEST BUTTON");
            return "redirect:/Profil?id=" + id;
		}
    	else { 
    		return "redirect:/Uebersicht?id=" + id;
    	}
    }

    	
    
}

