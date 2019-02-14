package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArtikelerstellungController {

    @Autowired
    ArtikelRepository artikelRepository;

    @Autowired
    DataManager dataManager;

    @GetMapping("/Erstellen")
    public String greetingForm(Long id, Model model) {

        if(id==null){
            return "redirect:/";
        }
        Benutzer benutzer = dataManager.getBenutzerById(id);
        model.addAttribute("artikel", new Artikel());
        model.addAttribute("benutzer", benutzer);
        return "Artikelerstellung";
    }

    @PostMapping("/Erstellen")
    public String personSubmitStart(Long id, @ModelAttribute Artikel artikel, Model model) {
        /*if(id==null) {
            return "redirect:/";
        }*/
        Benutzer benutzer = dataManager.getBenutzerById(id);
        artikel.setBenutzer(benutzer);
        artikelRepository.save(artikel);
        model.addAttribute("artikel", artikelRepository.findAll());
        model.addAttribute("benutzer", benutzer);
        return "redirect:/Uebersicht?id=" + benutzer.getBenutzerId();
    }

}
