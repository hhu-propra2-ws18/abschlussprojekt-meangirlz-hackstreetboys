package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;

@Component
public class DataInitializer implements ServletContextInitializer {

    @Autowired
    DataManager dataM;

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        System.out.println("Populating the database");

        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("Jemail@hhu.de");
        b0.setBenutzerName("Jens");
        b0.setArtikel(new ArrayList<Artikel>());
        dataM.erstelleBenutzer(b0);

        Benutzer b1 = new Benutzer();
        b1.setBenutzerEmail("scam@scamer.com");
        b1.setBenutzerName("Nashorn");
        b1.setArtikel(new ArrayList<Artikel>());
        dataM.erstelleBenutzer(b1);

        Benutzer b2 = new Benutzer();
        b1.setBenutzerEmail("nashorn@wgpartner.nh");
        b1.setBenutzerName("Antoine");
        b1.setArtikel(new ArrayList<Artikel>());
        dataM.erstelleBenutzer(b1);

        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("Einfach hammer dieser Hammer!");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Jens's-Werkstatt, a.k.a. HHU");
        a0.setArtikelTarif(1);
        Long bId = dataM.findBenutzerByName("Jens").getBenutzerId();
        dataM.erstelleArtikel(bId,a0);

        Artikel a1 = new Artikel();
        a1.setArtikelBeschreibung("Wohn mit mir zusammen");
        a1.setArtikelKaution(50);
        a1.setArtikelName("Ich - das Nashorn");
        a1.setArtikelOrt("wo du wohnst");
        a1.setArtikelTarif(1);
        Long bId2 = dataM.findBenutzerByName("Nashorn").getBenutzerId();
        dataM.erstelleArtikel(bId2,a1);

    }
}
