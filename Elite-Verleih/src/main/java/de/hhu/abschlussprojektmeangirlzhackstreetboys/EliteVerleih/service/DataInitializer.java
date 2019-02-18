package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DataInitializer implements ServletContextInitializer {

    @Autowired
    BenutzerManager benutzerM;

    @Autowired
    ArtikelManager artikelM;
    
    @Autowired
    AusleiheManager ausleiheM;

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        System.out.println("Populating the database");

        Benutzer support = new Benutzer();
        support.setBenutzerEmail("support@hhu.de");
        support.setBenutzerName("support");
        support.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(support);
        Long supportId= benutzerM.findBenutzerByName("support").getBenutzerId();

        Benutzer bJens = new Benutzer();
        bJens.setBenutzerEmail("Jemail@hhu.de");
        bJens.setBenutzerName("Jens");
        bJens.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(bJens);
        Long jensId = benutzerM.findBenutzerByName("Jens").getBenutzerId();

        Benutzer bAntoine = new Benutzer();
        bAntoine.setBenutzerEmail("nashorn@wgpartner.nh");
        bAntoine.setBenutzerName("Antoine");
        bAntoine.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(bAntoine);
        Long antoineId = benutzerM.findBenutzerByName("Antoine").getBenutzerId();

        Benutzer bTimo = new Benutzer();
        bTimo.setBenutzerEmail("timo@edithackstreetboys.de");
        bTimo.setBenutzerName("Timo");
        bTimo.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(bTimo);
        Long timoId = benutzerM.findBenutzerByName("Timo").getBenutzerId();

        Benutzer bSchwarzmarktVerkäufer = new Benutzer();
        bSchwarzmarktVerkäufer.setBenutzerEmail("scam@yohoo.de");
        bSchwarzmarktVerkäufer.setBenutzerName("SchwarzmarktVerkäufer");
        bSchwarzmarktVerkäufer.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(bSchwarzmarktVerkäufer);
        Long sVId = benutzerM.findBenutzerByName("SchwarzmarktVerkäufer").getBenutzerId();

        Benutzer bMafiaUser07 = new Benutzer();
        bMafiaUser07.setBenutzerEmail("mafioso@mafia.it");
        bMafiaUser07.setBenutzerName("MafiaUser07");
        bMafiaUser07.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(bMafiaUser07);
        Long mUId = benutzerM.findBenutzerByName("MafiaUser07").getBenutzerId();

        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("Einfach hammer dieser Hammer!");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Jens's-Werkstatt, a.k.a. HHU");
        a0.setArtikelTarif(1);
        artikelM.erstelleArtikel(jensId,a0);

        Artikel a1 = new Artikel();
        a1.setArtikelBeschreibung("Wohn mit mir zusammen");
        a1.setArtikelKaution(500000);
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

        Artikel a6_longText = new Artikel();
        a6_longText.setArtikelBeschreibung("Perfekt zum Zerkleinern jeglicher Gartenabfälle, " +
                "pflanzlicher Überreste, Blätter, Grashalme, kleine bis grosse Bäume, Informanten, " +
                "Geiseln. Sträucher, Hecken, sowie dem Buchsbaum. Dazu ist die Schere fast noch " +
                "nie benutzt worden, und nach ihrer letzten Benutzung sehr ordentlich gesäubert worden.");
        a6_longText.setArtikelKaution(300);
        a6_longText.setArtikelName("Gartenschere");
        a6_longText.setArtikelOrt("Pizzeria Vapioso");
        a6_longText.setArtikelTarif(30);
        artikelM.erstelleArtikel(mUId,a6_longText);
        
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        Ausleihe test =  ausleiheM.erstelleAusleihe(new Long(1),new Long(7), sD0, eD0);

        test.setAusleihStatus(Status.KONFLIKT);
        
    }
}
