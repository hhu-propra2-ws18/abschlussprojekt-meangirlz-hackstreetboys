package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.assertj.AssertableReactiveWebApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;

@Import({AusleiheManager.class})
@RunWith(SpringRunner.class)
@DataJpaTest
public class AusleiheManagerTest {

    @Autowired
    AusleiheManager ausleiheM;

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    ArtikelRepository artikelRepo;

    @Rollback
    @Test
    public void createAusleihe_RichtigeMappings_Benutzer(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        b0 = benutzerRepo.save(b0);
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        a0.setBenutzer(b0);
        a0 = artikelRepo.save(a0);
        Ausleihe ausleihe = new Ausleihe();
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        ausleiheM.erstelleAusleihe(benutzerRepo.findBenutzerByBenutzerName("test").get().getBenutzerId(),
                artikelRepo.findArtikelByArtikelName("Hammer").get().getArtikelId(),sD0,eD0);
        Assertions.assertThat(benutzerRepo.findBenutzerByBenutzerName("test").get().getAusgeliehen().size()==1);
    }

    @Rollback
    @Test
    public void createAusleihe_RichtigeMappings_Artikel(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        b0 = benutzerRepo.save(b0);
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        a0.setBenutzer(b0);
        a0 = artikelRepo.save(a0);
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        Ausleihe ausleihe = ausleiheM.erstelleAusleihe(b0.getBenutzerId(), a0.getArtikelId(), sD0, eD0);
        Assertions.assertThat(a0.getAusgeliehen().size()==1);
    }

    @Rollback
    @Test
    public void deleteAusleihe_RichtigeMappings_AusleiheinBesitzer(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("Ausleiher");
        b0.setAusgeliehen(new ArrayList<Ausleihe>());
        b0 = benutzerRepo.save(b0);
        Benutzer b1 = new Benutzer();
        b1.setBenutzerEmail("test@yahoo");
        b1.setBenutzerName("Besitzer");
        b1.setArtikel(new ArrayList<Artikel>());
        b1 = benutzerRepo.save(b1);
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        a0.setBenutzer(b1);
        a0 = artikelRepo.save(a0);
        b1.getArtikel().add(a0);
        benutzerRepo.save(b1);
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        Ausleihe ausleihe = ausleiheM.erstelleAusleihe(b0.getBenutzerId(), a0.getArtikelId(), sD0, eD0);

        ausleiheM.loescheAusleihe(ausleihe.getAusleihId());
        for(Artikel a : benutzerRepo.findBenutzerByBenutzerId(b1.getBenutzerId()).getArtikel()){
            if(a.getArtikelId()==a0.getArtikelId()){
                System.err.println("if erreicht!!!");
                Assertions.assertThat(!a.getAusgeliehen().contains(ausleihe));
            }
        }
    }

    @Rollback
    @Test
    public void deleteAusleihe_RichtigeMappings_AusleiheinAusleiher(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("Ausleiher");
        b0.setAusgeliehen(new ArrayList<Ausleihe>());
        b0 = benutzerRepo.save(b0);
        Benutzer b1 = new Benutzer();
        b1.setBenutzerEmail("test@yahoo");
        b1.setBenutzerName("Besitzer");
        b1.setArtikel(new ArrayList<Artikel>());
        b1 = benutzerRepo.save(b1);
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        a0.setBenutzer(b1);
        a0 = artikelRepo.save(a0);
        b1.getArtikel().add(a0);
        benutzerRepo.save(b1);
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        Ausleihe ausleihe = ausleiheM.erstelleAusleihe(b0.getBenutzerId(), a0.getArtikelId(), sD0, eD0);

        ausleiheM.loescheAusleihe(ausleihe.getAusleihId());
        Assertions.assertThat(b0.getAusgeliehen().contains(ausleihe));
    }
}

