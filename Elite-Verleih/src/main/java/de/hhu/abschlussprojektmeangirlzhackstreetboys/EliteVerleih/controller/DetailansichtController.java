package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.AusleiheRepository;
//import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DetailansichtController {

    @Autowired
    ArtikelRepository artikelRepo;

    @Autowired
    AusleiheRepository ausleiheRepo;


    @GetMapping("/Detailansicht/{id}")
    public String DetailansichtAnzeigen(@PathVariable long id, Model model){
        model.addAttribute("artikel", artikelRepo.findArtikelByArtikelId(id));
        return "Detailansicht";
    }

    @GetMapping("/Detailansicht/{id}")
    public String erstelleAusleihe(@PathVariable long id, Model model){
        model.addAttribute("ausleihe", ausleiheRepo.findAusleiheByAusleiheId(id));
        return "Detailansicht";
    }

    @PostMapping(" /Detailansicht/{id}")
    public String ausleiheSubmitStart(@ModelAttribute Ausleihe ausleihe, Model model){
        ausleiheRepo.save(ausleihe);

        model.addAttribute("ausleiheRepo", ausleiheRepo.findAll());
        return "redirect://localhost:8080/Detailansicht";
    }




}
