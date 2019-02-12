package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArtikelerstellungController {

    @GetMapping("/Erstellen")
    public String ArtikelbestellenAnzeigen(){
        return "Artikelerstellung";
    }
}
