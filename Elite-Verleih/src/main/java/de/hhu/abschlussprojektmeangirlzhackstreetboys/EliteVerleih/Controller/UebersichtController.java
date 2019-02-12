package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.Controller;


import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.server.PathParam;

@Controller
public class UebersichtController {

    @Autowired
    DataManager dataManager;

    @GetMapping("/Uebersicht")
    public String UebersichtAnzeigen(Model model,
                                     Long id){


        Benutzer benutzer = dataManager.getBenutzerById(id);

        model.addAttribute("benutzer",benutzer);

        return "Uebersicht";
    }
}
