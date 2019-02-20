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

	DataSync sync = new DataSync();

    @GetMapping("/Profil")
    public String ProfilAnzeigen(Long id, Model model){
    	if(id == null) {
    		return "redirect:/";
    	}
    	
    	Benutzer benutzer = benutzerManager.getBenutzerById(id);
    	model.addAttribute("benutzer",benutzer);
    	List<Ausleihe> wartend = benutzerManager.sucheAnfragen(benutzer, Status.ANGEFRAGT);
    	List<Ausleihe> zurueckerhaltene = benutzerManager.sucheAnfragen(benutzer, Status.ABGEGEBEN);
    	List<Ausleihe> konflikte = (benutzerManager.sucheAnfragen(benutzer, Status.KONFLIKT));
    	List<Ausleihe> bestaetigte = benutzerManager.sucheEigeneAnfragen(benutzer, Status.BESTAETIGT);
    	List<Ausleihe> zurueckgegebene = benutzerManager.sucheEigeneAnfragen(benutzer, Status.ABGEGEBEN);
    	List<Ausleihe> verliehenes = benutzerManager.sucheAnfragen(benutzer, Status.BESTAETIGT);
    	verliehenes.addAll(benutzerManager.sucheAnfragen(benutzer, Status.AKTIV));
    	verliehenes.addAll(benutzerManager.sucheAnfragen(benutzer, Status.KONFLIKT));
    	List<Ausleihe> erfolgreichZurueckgegeben = benutzerManager.sucheEigeneAnfragen(benutzer, Status.BEENDET);
    	List<Ausleihe> eigeneAnfragen = benutzerManager.sucheEigeneAnfragen(benutzer, Status.ANGEFRAGT);
    	int geld = (int) sync.getAccount(benutzer.getBenutzerName()).getAmount();

    	model.addAttribute("wartendeAnfragen", eigeneAnfragen);
		model.addAttribute("erfolgreichZurueckgegebene", erfolgreichZurueckgegeben);
    	model.addAttribute("verliehenes", verliehenes);
    	model.addAttribute("zurueckerhaltene", zurueckerhaltene);
    	model.addAttribute("zurueckgegebene", zurueckgegebene);
    	model.addAttribute("bestaetigte", bestaetigte);
		model.addAttribute("anfragen", wartend);
		model.addAttribute("konflikte", konflikte);
		model.addAttribute("Betrag", geld);

        return "Profil";
    }
    
    @PostMapping("/Profil")
    public String profilAnzeigen(Long id, @RequestParam(value= "submitButton") String name,
    		 Model model, @ModelAttribute Ausleihe anfrage, Long ausleihId) {
    	if (name.equals("Problem")) {
    		return "redirect:/Support?id=" + id;
    	}

		if (name.equals("Bestaetigen")) {
			ausleiheManager.bestaetigeAusleihe(ausleihId);
            return "redirect:/Profil?id=" + id;
		} else if (name.equals("Ablehnen")) {
			ausleiheManager.bearbeiteAusleihe(ausleihId,Status.ABGELEHNT);
			return "redirect:/Profil?id=" + id;
		} else if(name.equals("Zurueckgeben")) {
			ausleiheManager.zurueckGeben(ausleihId);
			return "redirect:/Profil?id=" + id;
		} else if(name.equals("Akzeptieren")){
			ausleiheManager.bearbeiteAusleihe(ausleihId,Status.BEENDET);
			return "redirect:/Profil?id=" + id;
		} else if(name.equals("Entfernen")){
			ausleiheManager.loescheAusleihe(ausleihId);
			return "redirect:/Profil?id=" + id;
		} else if(name.equals("Zurueckziehen")){
		    ausleiheManager.loescheAusleihe(ausleihId);
            return "redirect:/Profil?id=" + id;
        } else if(name.equals("Konflikt")){
		    ausleiheManager.konfliktAusleihe(ausleihId);
            return "redirect:/Profil?id=" + id;
        } else if(name.equals("Geloest")){
			ausleiheManager.bearbeiteAusleihe(ausleihId,Status.BEENDET);
			return "redirect:/Profil?id=" + id;
		}
    	else { 
    		return "redirect:/Uebersicht?id=" + id;
    	}
    }

    	
    
}

