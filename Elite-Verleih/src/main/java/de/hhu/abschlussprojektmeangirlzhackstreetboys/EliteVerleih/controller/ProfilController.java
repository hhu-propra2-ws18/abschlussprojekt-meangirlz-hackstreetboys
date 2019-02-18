package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Status;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.ArtikelManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.AusleiheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	@Autowired
	ArtikelManager artikelManager;

    @GetMapping("/Profil")
    public String ProfilAnzeigen(Long id, Model model){
    	if(id == null) {
    		return "redirect:/";
    	}
    	
    	Benutzer benutzer = benutzerManager.getBenutzerById(id);
    	model.addAttribute("benutzer",benutzer);
    	List<Ausleihe> wartend = benutzerManager.sucheAnfragen(benutzer, Status.ANGEFRAGT);
    	List<Ausleihe> zurueckerhaltene = benutzerManager.sucheAnfragen(benutzer, Status.ABGEGEBEN);
    	List<Ausleihe> bestaetigte = benutzerManager.sucheEigeneAnfragen(benutzer, Status.BESTAETIGT);
    	List<Ausleihe> zurueckgegebene = benutzerManager.sucheEigeneAnfragen(benutzer, Status.ABGEGEBEN);
    	List<Ausleihe> verliehenes = benutzerManager.sucheAnfragen(benutzer, Status.BESTAETIGT);
    	verliehenes.addAll(benutzerManager.sucheAnfragen(benutzer, Status.AKTIV));
    	List<Ausleihe> erfolgreichZurueckgegeben = benutzerManager.sucheEigeneAnfragen(benutzer, Status.BEENDET);

		model.addAttribute("erfolgreichZurueckgegebene", erfolgreichZurueckgegeben);
    	model.addAttribute("verliehenes", verliehenes);
    	model.addAttribute("zurueckerhaltene", zurueckerhaltene);
    	model.addAttribute("zurueckgegebene", zurueckgegebene);
    	model.addAttribute("bestaetigte", bestaetigte);
		model.addAttribute("anfragen", wartend);

        return "Profil";
    }
    
    @PostMapping("/Profil")
    public String profilAnzeigen(Long id, @RequestParam(value= "submitButton") String name,
    		 Model model, @ModelAttribute Ausleihe anfrage, Long ausleihId) {
    	if (name.equals("Problem")) {
    		return "redirect:/Support?id=" + id;
    	}

		if (name.equals("Bestaetigen")) {
			Benutzer benutzer = benutzerManager.getBenutzerById(id);
			Ausleihe ausleihe = ausleiheManager.getAusleiheById(ausleihId);
			ausleiheManager.bestaetigeAusleihe(ausleihe);
            return "redirect:/Profil?id=" + id;
		} else if (name.equals("Ablehnen")) {
			Ausleihe ausleihe = ausleiheManager.getAusleiheById(ausleihId);
			ausleiheManager.lehneAusleiheAb(ausleihe);
			return "redirect:/Profil?id=" + id;
		} else if(name.equals("Zurueckgeben")) {
			Ausleihe ausleihe = ausleiheManager.getAusleiheById(ausleihId);
			ausleiheManager.zurueckGeben(ausleihe);
			return "redirect:/Profil?id=" + id;
		} else if(name.equals("Akzeptieren")){
			Ausleihe ausleihe = ausleiheManager.getAusleiheById(ausleihId);
			ausleiheManager.rueckgabeAkzeptieren(ausleihe);
			return "redirect:/Profil?id=" + id;
		} else if(name.equals("Entfernen")){
			//ausleiheManager.loescheAusleihe(ausleihId);
			return "redirect:/Profil?id=" + id;
		}
    	else { 
    		return "redirect:/Uebersicht?id=" + id;
    	}
    }

    	
    
}

