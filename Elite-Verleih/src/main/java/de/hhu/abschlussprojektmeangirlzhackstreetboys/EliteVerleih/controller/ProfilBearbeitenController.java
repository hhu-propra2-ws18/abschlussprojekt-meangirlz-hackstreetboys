package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.DataManager;

@Controller
public class ProfilBearbeitenController {
	@Autowired
	DataManager dataManager;
    @GetMapping("/ProfilBearbeiten")
    public String ProfilBearbeitenAnzeigen(Long id, Model model){
    	Benutzer benutzer = dataManager.getBenutzerById(id);
    	model.addAttribute("benutzer",benutzer);
		return "ProfilBearbeiten";
    }
}
