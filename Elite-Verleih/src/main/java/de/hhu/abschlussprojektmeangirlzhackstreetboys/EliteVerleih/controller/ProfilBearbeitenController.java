package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.DataManager;

@Controller
public class ProfilBearbeitenController {
	@Autowired
	DataManager dataManager;
	
    @GetMapping("/ProfilBearbeiten")
    public String ProfilBearbeitenAnzeigen(Long id, Model model){
    	if(id == null) {
    		return "redirect:/";
    	}
    	Benutzer benutzer = dataManager.getBenutzerById(id);
    	model.addAttribute("benutzer",benutzer);
		return "ProfilBearbeiten";
    }
    @PostMapping("/ProfilBearbeiten")
    public String ProfilBearbeitenSpeichern(Long id, @ModelAttribute Benutzer newBenutzer, Model model){
    	if(id == null) {
    		return "redirect:/";
    	}
    	Benutzer benutzer = dataManager.getBenutzerById(id);
    	model.addAttribute("benutzer",benutzer);
		return "redirect:/Uebersicht?id=" + benutzer.getBenutzerId();
    }
}
