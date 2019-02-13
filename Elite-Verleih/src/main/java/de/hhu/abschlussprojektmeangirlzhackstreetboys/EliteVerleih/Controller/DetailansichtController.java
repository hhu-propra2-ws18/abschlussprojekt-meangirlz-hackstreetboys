package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Artikel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailansichtController {

    @Autowired
    ArtikelRepository artikel;


    @GetMapping("/Detailansicht/{id}")
    public String DetailansichtAnzeigen(@PathVariable long id, Model model){
        model.addAttribute("artikel", artikel.findArtikelByArtikelId(id) );
        return "Detailansicht";
    }


}
