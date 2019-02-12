package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DetailansichtController {

    @Autowired
    ArtikelRepository artikel;


    @GetMapping("/Detailansicht")
    public String DetailansichtAnzeigen(Model model){
        model.addAttribute("artikel", artikel.findAll() );
        return "Detailansicht";
    }


}
