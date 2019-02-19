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
        a0.setArtikelBildURL("https://upload.wikimedia.org/wikipedia/commons/b/b8/Patent-Motorwagen_Nr.1_Benz_2.jpg");
        artikelM.erstelleArtikel(jensId,a0);

        Artikel a1 = new Artikel();
        a1.setArtikelBeschreibung("Wohn mit mir zusammen");
        a1.setArtikelKaution(50000);
        a1.setArtikelName("Ich - das Nashorn");
        a1.setArtikelOrt("wo du wohnst");
        a1.setArtikelTarif(1);
        a1.setArtikelBildURL("https://static.geo.de/bilder/ef/25/11530/article_image_big/b2e2b87b03bb10e6bd37f28957c7c246.jpg");
        artikelM.erstelleArtikel(antoineId,a1);

        Artikel a2 = new Artikel();
        a2.setArtikelBeschreibung("Ich habe mit diesem Stift einmal FAST eine 1 geschrieben.");
        a2.setArtikelKaution(100);
        a2.setArtikelName("Timo's Stift");
        a2.setArtikelOrt("Timo's Bude");
        a2.setArtikelTarif(1);
        a2.setArtikelBildURL("https://www.merkur.de/bilder/2017/05/09/8281587/1578562437-waehrend-start-landung-gehen-flugzeug-lichter-aus-doch-wieso-X1DHAIVKyNG.jpg");
        artikelM.erstelleArtikel(timoId,a2);

        Artikel a3 = new Artikel();
        a3.setArtikelBeschreibung("Ich bin fertig damit, aber wenn sonst einer das will..");
        a3.setArtikelKaution(1000000);
        a3.setArtikelName("Timo's altes Kaugummi");
        a3.setArtikelOrt("Timo's Bude");
        a3.setArtikelTarif(10000);
        a3.setArtikelBildURL("https://images.lecker.de/,id=58212d91,b=lecker,w=610,cg=c.jpg");
        artikelM.erstelleArtikel(timoId,a3);

        Artikel a4 = new Artikel();
        a4.setArtikelBeschreibung("ORIGINAL!!! Bester Preis DIGGAHH!");
        a4.setArtikelKaution(10000);
        a4.setArtikelName("ROLLLEX");
        a4.setArtikelOrt("PN");
        a4.setArtikelTarif(100);
        a4.setArtikelBildURL("https://www.bettmer.de/out/pictures/generated/product/1/395_395_90/2-in-1-stift-clic-clac-teruel_dunkelgrau_300011_302097_1.jpg");
        artikelM.erstelleArtikel(sVId,a4);

        Artikel a5 = new Artikel();
        a5.setArtikelBeschreibung("ORIGINAL!!! Bester Preis mann!");
        a5.setArtikelKaution(10000);
        a5.setArtikelName("Armanni Hemd");
        a5.setArtikelOrt("PN");
        a5.setArtikelTarif(100);
        a5.setArtikelBildURL("https://www.bfh24.eu/images/artikel/zoom/120563150-0-Staedtler-Metall-Lineal-15cm563-15.jpg");
        artikelM.erstelleArtikel(sVId,a5);

        Artikel a7 = new Artikel();
        a7.setArtikelBeschreibung("ORIGINAL!!! 2. bester Preis JUNGE!");
        a7.setArtikelKaution(10000);
        a7.setArtikelName("Guci Hemd");
        a7.setArtikelOrt("PN");
        a7.setArtikelTarif(100);
        a7.setArtikelBildURL("https://static.tonight.de/thumbs/img/News/16/77/99/p/p_pano/london-feuerwehr-will-papagei-retten-und-wird-von-ihm-beschimpft-997716.jpg");
        artikelM.erstelleArtikel(sVId,a7);

        Artikel a8 = new Artikel();
        a8.setArtikelBeschreibung("ORIGINAL!!! Bester Preis mann!");
        a8.setArtikelKaution(10000);
        a8.setArtikelName("Pradda Hemd");
        a8.setArtikelOrt("PN");
        a8.setArtikelTarif(100);
        a8.setArtikelBildURL("https://www.veolia.de/sites/g/files/dvc1936/f/styles/custom_resize/public/2017/02/400x0/Ratte.jpg");
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
        a6_longText.setArtikelBildURL("https://cdn02.plentymarkets.com/bl8cypz80juz/item/images/917667/full/Edelstahl-Automatik-Muelleimer---Abfalleimer-18-Li.jpg");
        artikelM.erstelleArtikel(mUId,a6_longText);
        
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        Ausleihe test0 =  ausleiheM.erstelleAusleihe(new Long(1),new Long(7), sD0, eD0);

        Date sD1 = new Date(119, 1, 19);
        Date eD1 = new Date(119, 1, 28);
        Ausleihe test1 =  ausleiheM.erstelleAusleihe(new Long(3),new Long(7), sD1, eD1);

        
        Ausleihe test =  ausleiheM.erstelleAusleihe(new Long(1),new Long(9), sD0, eD0);
        Ausleihe testtest = ausleiheM.erstelleAusleihe(new Long(5),new Long(10), sD0, eD0);

        ausleiheM.setzeSatusAusleihe(test, "KONFLIKT");
        ausleiheM.setzeSatusAusleihe(testtest, "KONFLIKT");
    }
}
