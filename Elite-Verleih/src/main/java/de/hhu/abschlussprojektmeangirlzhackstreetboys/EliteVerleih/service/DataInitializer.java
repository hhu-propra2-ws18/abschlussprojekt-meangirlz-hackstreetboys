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
        artikelM.erstelleArtikel(antoineId,a1);

        Artikel a2 = new Artikel();
        a2.setArtikelBeschreibung("Ich habe mit diesem Stift einmal FAST eine 1 geschrieben.");
        a2.setArtikelKaution(100);
        a2.setArtikelName("Timo's Stift");
        a2.setArtikelOrt("Timo's Bude");
        a2.setArtikelTarif(1);
        artikelM.erstelleArtikel(timoId,a2);

        Artikel a3 = new Artikel();
        a3.setArtikelBeschreibung("Ich bin fertig damit, aber wenn sonst einer das will..");
        a3.setArtikelKaution(1000000);
        a3.setArtikelName("Timo's altes Kaugummi");
        a3.setArtikelOrt("Timo's Bude");
        a3.setArtikelTarif(10000);
        artikelM.erstelleArtikel(timoId,a3);

        Artikel a4 = new Artikel();
        a4.setArtikelBeschreibung("ORIGINAL!!! Bester Preis mann!");
        a4.setArtikelKaution(10000);
        a4.setArtikelName("ROLLLEX");
        a4.setArtikelOrt("PN");
        a4.setArtikelTarif(100);
        artikelM.erstelleArtikel(sVId,a4);

        Artikel a5 = new Artikel();
        a5.setArtikelBeschreibung("ORIGINAL!!! Bester Preis mann!");
        a5.setArtikelKaution(10000);
        a5.setArtikelName("Armanni Hemd");
        a5.setArtikelOrt("PN");
        a5.setArtikelTarif(100);
        artikelM.erstelleArtikel(sVId,a5);

        Artikel a7 = new Artikel();
        a7.setArtikelBeschreibung("ORIGINAL!!! Bester Preis mann!");
        a7.setArtikelKaution(10000);
        a7.setArtikelName("Guci Hemd");
        a7.setArtikelOrt("PN");
        a7.setArtikelTarif(100);
        artikelM.erstelleArtikel(sVId,a7);

        Artikel a8 = new Artikel();
        a8.setArtikelBeschreibung("ORIGINAL!!! Bester Preis mann!");
        a8.setArtikelKaution(10000);
        a8.setArtikelName("Pradda Hemd");
        a8.setArtikelOrt("PN");
        a8.setArtikelTarif(100);
        artikelM.erstelleArtikel(sVId,a8);

        Artikel a6 = new Artikel();
        a6.setArtikelBeschreibung("Perfekt zum Zerkleinern jeglicher Gartenabfälle, pflanzlicher Überreste, Blätter, Grashalme, " +
                "kleine bis grosse Bäume, Informanten, Geiseln, Sträucher, Hecken, sowie dem Buchsbaum." +
                "Dazu ist die Schere fast noch nie benutzt worden, und nach ihrer letzten Benutzung sehr ordentlich gesäubert worden.");
        a6.setArtikelKaution(300);
        a6.setArtikelName("Gartenschere");
        a6.setArtikelOrt("Pizzeria Vapioso");
        a6.setArtikelTarif(30);
        artikelM.erstelleArtikel(sVId,a6);

    }
}
