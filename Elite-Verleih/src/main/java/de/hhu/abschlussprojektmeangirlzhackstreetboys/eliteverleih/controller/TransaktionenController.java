package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Transaktion;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.PropayManager;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.TransaktionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Max;
import java.security.Principal;
import java.util.List;

@Controller
public class TransaktionenController {

    BenutzerManager benutzerManager;

    TransaktionManager transaktionManager;

    PropayManager propayManager;


    @Autowired
    public TransaktionenController(BenutzerManager benutzerManager,
                                   TransaktionManager transaktionManager,
                                   PropayManager propayManager){
        this.benutzerManager = benutzerManager;
        this.transaktionManager =transaktionManager;
        this.propayManager = propayManager;
    }


    /**
     * Zeigt die Transaktionenseite an
     *
     * @param model         Datencontainer für die View
     * @param account       Account des aktuellen Users
     * @return "Transaktionen"
     */

    @GetMapping("/Transaktionen")
    public String transaktionenAnzeigen(Model model, Principal account){
        Benutzer benutzer = benutzerManager.findBenutzerByName(account.getName());
        model.addAttribute("benutzer", benutzer);

        List<Transaktion> transaktion = transaktionManager.getAllTransaktion(account.getName());
        model.addAttribute("transaktion", transaktion);

        AccountDto account1 = propayManager.getAccount(benutzer.getBenutzerName());
        if (account1 == null) {
            model.addAttribute("Betrag", "Propay nicht erreichbar xx,xx");
        } else {
            double betrag = account1.getAmount();
            model.addAttribute("Betrag", betrag);
        }

        return "Transaktionen";
    }
}
