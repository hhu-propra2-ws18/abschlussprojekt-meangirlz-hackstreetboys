package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    
    @PostMapping("/Profil")
    public String profilAnzeigen(Long id, @RequestParam(value= "submitButton") String name,
    		 Model model) {
    	if (name.equals("Problem")) {
    		System.out.println(id);
    		return "redirect:/Support?id=" + id;
    	}
    	else { 
    		return "redirect:/Uebersicht?id=" + id;
    	}
    }
    	
    
}

