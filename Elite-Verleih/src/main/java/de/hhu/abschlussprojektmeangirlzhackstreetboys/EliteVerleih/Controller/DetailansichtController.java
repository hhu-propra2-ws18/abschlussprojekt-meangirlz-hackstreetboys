package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DetailansichtController {

    @GetMapping("/Detailansicht")
    public String DetailansichtAnzeigen(){
        return "Detailansicht";
    }
}
