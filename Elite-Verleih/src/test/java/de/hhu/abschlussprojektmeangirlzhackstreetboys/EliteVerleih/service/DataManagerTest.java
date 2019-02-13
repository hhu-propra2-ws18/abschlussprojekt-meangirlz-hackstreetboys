package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
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

@Import({DataManager.class})
@RunWith(SpringRunner.class)
@DataJpaTest
public class DataManagerTest {

    @Autowired
    DataManager dataM;

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    BenutzerRepository artikelRepo;

    @Rollback
    @Test
    public void createBenutzer_EqualName(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        dataM.erstelleBenutzer(b0);
        Assertions.assertThat(benutzerRepo.findAll().get(0).getBenutzerName().equals("test"));
    }

    @Rollback
    @Test
    public void createBenutzer_EqualEmail(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        dataM.erstelleBenutzer(b0);
        Assertions.assertThat(benutzerRepo.findAll().get(0).getBenutzerEmail().equals("test@yahoo"));
    }

    @Rollback
    @Test
    public void bearbeiteBenutzer_EqualName(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        dataM.erstelleBenutzer(b0);
        Long bId = dataM.findBenutzerByName("test").getBenutzerId();
        dataM.bearbeiteBenutzer(bId,b0);
        Assertions.assertThat(benutzerRepo.findAll().get(0).getBenutzerEmail().equals("geändert!"));
    }

    @Rollback
    @Test
    public void createArtikel_EqualName(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        dataM.erstelleBenutzer(b0);
        Long bId = dataM.findBenutzerByName("test").getBenutzerId();
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        dataM.erstelleArtikel(bId,a0);
        Assertions.assertThat(dataM.getAllArtikel().get(0).getArtikelName().equals("Hammer"));
    }

    @Rollback
    @Test
    public void nameAlreadyExists_AlreadyExists(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        dataM.erstelleBenutzer(b0);
        Assertions.assertThat(dataM.nameSchonVorhanden("test")==true);
    }

    @Rollback
    @Test
    public void nameAlreadyExists_IsNew(){
        Assertions.assertThat(dataM.nameSchonVorhanden("test")==false);
    }
}
