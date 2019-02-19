package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;

@Controller
public class ProfilBearbeitenController {
	@Autowired
	BenutzerManager benutzerManager;
	
    @GetMapping("/ProfilBearbeiten")
    public String ProfilBearbeitenAnzeigen(Long id, Model model){
    	if(id == null) {
    		return "redirect:/";
    	}
    	Benutzer benutzer = benutzerManager.getBenutzerById(id);
    	model.addAttribute("benutzer",benutzer);
		return "ProfilBearbeiten";
    }

    @PostMapping("/ProfilBearbeiten")
    public String ProfilBearbeitenSpeichern(Long id, @ModelAttribute Benutzer ben, Model model, int aufladen){
    	if(id == null) {
    		return "redirect:/";
    	}

    	System.out.println(aufladen);
    	Benutzer benutzer = benutzerManager.getBenutzerById(id);
    	Benutzer newBenutzer = benutzerManager.editBenutzer(benutzer, ben.getBenutzerEmail());
    	model.addAttribute("benutzer",newBenutzer);
		return "redirect:/Profil?id=" + id;
    }
}
