package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.PropayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;

import java.security.Principal;

@Controller
public class ProfilBearbeitenController {
	@Autowired
	BenutzerManager benutzerManager;

    PropayManager sync = new PropayManager();

    @GetMapping("/ProfilBearbeiten")
    public String profilBearbeitenAnzeigen(Model model, Principal account){
    	Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        int geld = (int) sync.getAccount(benutzer.getBenutzerName()).getAmount();

        model.addAttribute("Betrag", geld);
    	model.addAttribute("benutzer",benutzer);
		return "ProfilBearbeiten";
    }

    @PostMapping("/ProfilBearbeiten")
    public String profilBearbeitenSpeichern(Model model, @ModelAttribute Benutzer ben, int aufladen, Principal account){
    	Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
    	Benutzer newBenutzer = benutzerManager.editBenutzer(benutzer, ben.getBenutzerEmail());
        benutzerManager.geldAufladen(newBenutzer, aufladen);
    	model.addAttribute("benutzer",newBenutzer);
		return "redirect:/Profil";
    }
}
