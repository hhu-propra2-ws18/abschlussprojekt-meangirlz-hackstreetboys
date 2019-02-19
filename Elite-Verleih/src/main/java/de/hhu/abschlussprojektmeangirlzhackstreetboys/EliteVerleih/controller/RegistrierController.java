package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrierController {

    @GetMapping("/registrieren")
    public String loginAnzeigen(){
        return "Registrierung";
    }
}
