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
        benutzerRepo.save(b0);
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        a0.setBenutzer(b0);
        artikelRepo.save(a0);
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
        benutzerRepo.save(b0);
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        a0.setBenutzer(b0);
        artikelRepo.save(a0);
        Ausleihe ausleihe = new Ausleihe();
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        ausleiheM.erstelleAusleihe(benutzerRepo.findBenutzerByBenutzerName("test").get().getBenutzerId(),
                artikelRepo.findArtikelByArtikelName("Hammer").get().getArtikelId(),sD0,eD0);
        Assertions.assertThat(artikelRepo.findArtikelByArtikelName("Hammer").get().getAusgeliehen().size()==1);
    }
}
