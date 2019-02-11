package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnmeldeController {
    @GetMapping("/")
    public String anzeigenView(){
        return "Anmeldung";
    }

}
