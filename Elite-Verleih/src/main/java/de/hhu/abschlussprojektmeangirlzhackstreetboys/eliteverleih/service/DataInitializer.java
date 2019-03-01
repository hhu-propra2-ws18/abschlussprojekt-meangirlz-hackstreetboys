package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class DataInitializer implements ServletContextInitializer {

    @Autowired
    BenutzerManager benutzerM;

    @Autowired
    ArtikelManager artikelM;

    @Autowired
    AusleiheManager ausleiheM;

    private String standardPasswort = "1";
    private String userRolle = "ROLE_USER";

    @Override
    public void onStartup(ServletContext servletContext)
        throws ServletException {
        System.out.println("Populating the database");

        Benutzer support = new Benutzer();
        support.setBenutzerEmail("support@hhu.de");
        support.setBenutzerName("support");
        support.setArtikel(new ArrayList<Artikel>());
        support.setBenutzerPasswort(standardPasswort);
        support.setBenutzerRolle("ROLE_SUPPORT");
        benutzerM.erstelleBenutzer(support);
        Long supportId = benutzerM.findBenutzerByName("support").getBenutzerId();

        Benutzer bJens = new Benutzer();
        bJens.setBenutzerEmail("Jemail@hhu.de");
        bJens.setBenutzerName("Jens");
        bJens.setArtikel(new ArrayList<Artikel>());
        bJens.setBenutzerPasswort(standardPasswort);
        bJens.setBenutzerRolle(userRolle);
        benutzerM.erstelleBenutzer(bJens);
        Long jensId = benutzerM.findBenutzerByName("Jens").getBenutzerId();

        Benutzer bAntoine = new Benutzer();
        bAntoine.setBenutzerEmail("nashorn@wgpartner.nh");
        bAntoine.setBenutzerName("Antoine");
        bAntoine.setArtikel(new ArrayList<Artikel>());
        bAntoine.setBenutzerPasswort(standardPasswort);
        bAntoine.setBenutzerRolle(userRolle);
        benutzerM.erstelleBenutzer(bAntoine);
        Long antoineId = benutzerM.findBenutzerByName("Antoine").getBenutzerId();

        Benutzer bTimo = new Benutzer();
        bTimo.setBenutzerEmail("timo@edithackstreetboys.de");
        bTimo.setBenutzerName("Timo");
        bTimo.setArtikel(new ArrayList<Artikel>());
        bTimo.setBenutzerPasswort(standardPasswort);
        bTimo.setBenutzerRolle(userRolle);
        benutzerM.erstelleBenutzer(bTimo);
        Long timoId = benutzerM.findBenutzerByName("Timo").getBenutzerId();

        Benutzer bSchwarzmarktVerkäufer = new Benutzer();
        bSchwarzmarktVerkäufer.setBenutzerEmail("scam@yohoo.de");
        bSchwarzmarktVerkäufer.setBenutzerName("SchwarzmarktVerkäufer");
        bSchwarzmarktVerkäufer.setArtikel(new ArrayList<Artikel>());
        bSchwarzmarktVerkäufer.setBenutzerPasswort(standardPasswort);
        bSchwarzmarktVerkäufer.setBenutzerRolle(userRolle);
        benutzerM.erstelleBenutzer(bSchwarzmarktVerkäufer);
        Long sVId = benutzerM.findBenutzerByName("SchwarzmarktVerkäufer").getBenutzerId();

        Benutzer bMafiaUser07 = new Benutzer();
        bMafiaUser07.setBenutzerEmail("mafioso@mafia.it");
        bMafiaUser07.setBenutzerName("MafiaUser07");
        bMafiaUser07.setArtikel(new ArrayList<Artikel>());
        bMafiaUser07.setBenutzerPasswort(standardPasswort);
        bMafiaUser07.setBenutzerRolle(userRolle);
        benutzerM.erstelleBenutzer(bMafiaUser07);
        Long mUId = benutzerM.findBenutzerByName("MafiaUser07").getBenutzerId();

        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("Einfach hammer dieser Hammer!");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Jens's-Werkstatt, a.k.a. HHU");
        a0.setArtikelOrtX(80);
        a0.setArtikelOrtY(10);
        a0.setArtikelTarif(1);
        a0.setArtikelBildUrl("https://c.pxhere.com/photos/c5/a6/hammer_car_automobile_nature_off_road-619096.jpg!d");
        artikelM.erstelleVerleihen(jensId, a0);

        Artikel a1 = new Artikel();
        a1.setArtikelBeschreibung("Wohn mit mir zusammen");
        a1.setArtikelKaution(50000);
        a1.setArtikelName("Ich - das Nashorn");
        a1.setArtikelOrt("");
        a1.setArtikelTarif(1);
        a1.setArtikelBildUrl("https://static.geo.de/bilder/ef/25/11530/article_image_big/b2e2b87b03bb10e6bd37f28957c7c246.jpg");
        artikelM.erstelleVerleihen(antoineId, a1);

        Artikel a2 = new Artikel();
        a2.setArtikelBeschreibung("Ich habe mit diesem Stift einmal FAST eine 1 geschrieben.");
        a2.setArtikelKaution(100);
        a2.setArtikelName("Timo's Stift");
        a2.setArtikelOrt("Mettmann Düsseldorf");
        a2.setArtikelTarif(1);
        a2.setArtikelBildUrl("https://www.bettmer.de/out/pictures/generated/product/1/395_395_90/2-in-1-stift-clic-clac-teruel_dunkelgrau_300011_302097_1.jpg");
        artikelM.erstelleVerleihen(timoId, a2);

        Artikel a3 = new Artikel();
        a3.setArtikelBeschreibung("Ich bin fertig damit, aber wenn sonst einer das will..");
        a3.setArtikelKaution(1000000);
        a3.setArtikelName("Timo's altes Kaugummi");
        a3.setArtikelOrt("Timo's Bude");
        a3.setArtikelTarif(10000);
        a3.setArtikelBildUrl("http://cdn3.spiegel.de/images/image-1341296-860_galleryfree-dshe-1341296.jpg");
        artikelM.erstelleVerleihen(timoId, a3);

        Artikel a4 = new Artikel();
        a4.setArtikelBeschreibung("ORIGINAL!!! Bester Preis DIGGAHH!");
        a4.setArtikelKaution(10000);
        a4.setArtikelName("ROLLLEX");
        a4.setArtikelOrt("PN");
        a4.setArtikelTarif(100);
        a4.setArtikelBildUrl("http://www.replicauhrendeutschland.com/wp-content/uploads/2018/04/Replica-Rolex-Submariner-Two-Tone.jpg");
        artikelM.erstelleVerleihen(sVId, a4);

        Artikel a5 = new Artikel();
        a5.setArtikelBeschreibung("ORIGINAL!!! Bester Preis mann!");
        a5.setArtikelKaution(10000);
        a5.setArtikelName("Armanni Hemd");
        a5.setArtikelOrt("PN");
        a5.setArtikelTarif(100);
        a5.setArtikelBildUrl("https://images-na.ssl-images-amazon.com/images/I/51d4N9L0qaL._UX385_.jpg");
        artikelM.erstelleVerleihen(sVId, a5);

        Artikel a7 = new Artikel();
        a7.setArtikelBeschreibung("ORIGINAL!!! 2. bester Preis JUNGE!");
        a7.setArtikelKaution(10000);
        a7.setArtikelName("Guci Hemd");
        a7.setArtikelOrt("PN");
        a7.setArtikelTarif(100);
        a7.setArtikelBildUrl("https://cms.brnstc.de/product_images/435x596/18/06/100080616717000_0.jpg");
        artikelM.erstelleVerleihen(sVId, a7);

        Artikel a8 = new Artikel();
        a8.setArtikelBeschreibung("ORIGINAL!!! Bester Preis mann!");
        a8.setArtikelKaution(10000);
        a8.setArtikelName("Pradda Hemd");
        a8.setArtikelOrt("PN");
        a8.setArtikelTarif(100);
        a8.setArtikelBildUrl("https://cdn.raffaello-network.com/deutsch/mode-detail/500827/350/prada-herrenbekleidung_pramclo-ucn2011tv41tv4-f0124-medium-1.jpg");
        artikelM.erstelleVerleihen(sVId, a8);

        Artikel a6_longText = new Artikel();
        a6_longText.setArtikelBeschreibung("Perfekt zum Zerkleinern jeglicher Gartenabfälle, " +
            "pflanzlicher Überreste, Blätter, Grashalme, kleine bis grosse Bäume, Informanten, " +
            "Geiseln. Sträucher, Hecken, sowie dem Buchsbaum. Dazu ist die Schere fast noch " +
            "nie benutzt worden, und nach ihrer letzten Benutzung sehr ordentlich gesäubert worden.");
        a6_longText.setArtikelKaution(300);
        a6_longText.setArtikelName("Gartenschere");
        a6_longText.setArtikelOrt("Pizzeria Vapios");
        a6_longText.setArtikelTarif(30);
        a6_longText.setArtikelBildUrl("https://static1.squarespace.com/static/58bf3d9a44024364324fb36f/58ff1ec19de4bb751e120a54/58ff33c9f5e2313f31104f1e/1493119955235/Sunken+Garden-032.jpg?format=500w");
        artikelM.erstelleVerleihen(mUId, a6_longText);


        Calendar sCal0 = new GregorianCalendar();
        sCal0.set(2019, 1, 8);
        Calendar eCal0 = new GregorianCalendar();
        eCal0.set(2019, 1, 10);
        Ausleihe test0 = ausleiheM.erstelleAusleihe(new Long(2), new Long(9), sCal0, eCal0);
        test0 = ausleiheM.bearbeiteAusleihe(test0.getAusleihId(), Status.BESTAETIGT);

        Calendar sCal1 = new GregorianCalendar();
        sCal1.set(2019, 2, 19);
        Calendar eCal1 = new GregorianCalendar();
        eCal1.set(2019, 2, 21);
        Ausleihe test1 = ausleiheM.erstelleAusleihe(new Long(3), new Long(7), sCal1, eCal1);

        Calendar sCal2 = new GregorianCalendar();
        sCal2.set(2019, 2, 22);
        Calendar eCal2 = new GregorianCalendar();
        eCal2.set(2019, 2, 23);
        Ausleihe test2 = ausleiheM.erstelleAusleihe(new Long(3), new Long(7), sCal2, eCal2);

        Ausleihe test = ausleiheM.erstelleAusleihe(new Long(4), new Long(9), sCal0, eCal0);

        Ausleihe testtest = ausleiheM.erstelleAusleihe(new Long(5), new Long(10), sCal0, eCal0);

        ausleiheM.bearbeiteAusleihe(test.getAusleihId(), Status.KONFLIKT);
    }
}
