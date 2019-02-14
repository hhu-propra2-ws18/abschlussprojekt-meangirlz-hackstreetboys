package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;

@Controller
public class ProfilController {
	@Autowired
    BenutzerManager benutzerManager;
    @GetMapping("/Profil")
    public String ProfilAnzeigen(Long id, Model model){
    	if(id == null) {
    		return "redirect:/";
    	}
    	Benutzer benutzer = benutzerManager.getBenutzerById(id);
    	model.addAttribute("benutzer",benutzer);
        return "Profil";
    }
}
