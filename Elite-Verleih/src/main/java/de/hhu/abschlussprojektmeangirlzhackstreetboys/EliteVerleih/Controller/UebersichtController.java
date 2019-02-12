package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UebersichtController {

    @GetMapping("/Uebersicht")
    public String UebersichtAnzeigen(){
        return "Uebersicht";
    }
}
