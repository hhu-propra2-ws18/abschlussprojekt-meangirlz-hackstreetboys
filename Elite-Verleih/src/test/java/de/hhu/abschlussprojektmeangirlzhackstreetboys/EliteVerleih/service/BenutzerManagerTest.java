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

@Import({BenutzerManager.class})
@RunWith(SpringRunner.class)
@DataJpaTest
public class BenutzerManagerTest {

  /*  @Autowired
    BenutzerManager benutzerM;

    @Autowired
    BenutzerRepository benutzerRepo;

    @Rollback
    @Test
    public void createBenutzer_EqualName(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(b0);
        Assertions.assertThat(benutzerRepo.findAll().get(0).getBenutzerName().equals("test"));
    }

    @Rollback
    @Test
    public void createBenutzer_EqualEmail(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(b0);
        Assertions.assertThat(benutzerRepo.findAll().get(0).getBenutzerEmail().equals("test@yahoo"));
    }

    @Rollback
    @Test
    public void bearbeiteBenutzer_EqualName(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(b0);
        Long bId = benutzerM.findBenutzerByName("test").getBenutzerId();
        benutzerM.bearbeiteBenutzer(bId,b0);
        Assertions.assertThat(benutzerRepo.findAll().get(0).getBenutzerEmail().equals("geändert!"));
    }

    @Rollback
    @Test
    public void nameAlreadyExists_AlreadyExists(){
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(b0);
        Assertions.assertThat(benutzerM.nameSchonVorhanden("test")==true);
    }

    @Rollback
    @Test
    public void nameAlreadyExists_IsNew(){
        Assertions.assertThat(benutzerM.nameSchonVorhanden("test")==false);
    }*/
}
