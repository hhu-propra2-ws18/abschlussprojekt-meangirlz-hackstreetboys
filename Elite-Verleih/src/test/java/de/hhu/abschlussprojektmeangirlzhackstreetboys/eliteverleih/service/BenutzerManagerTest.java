package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.AccountDto;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BenutzerManagerTest {

    static PropayManager propayManager = mock(PropayManager.class);


    BenutzerManager benutzerM;

    @Autowired
    BenutzerRepository benutzerRepo;

    @BeforeClass
    public static void setupBeforeClass() {
        AccountDto mockDto = new AccountDto();
        when(propayManager.getAccount(anyString())).thenReturn(mockDto);
    }

    @Before
    public void setup() {
        benutzerM = new BenutzerManager(benutzerRepo, propayManager);

    }

    @Rollback
    @Test
    public void erstelleBenutzer_EqualName() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(b0);
        Assertions.assertThat(benutzerRepo.findAll().get(0).getBenutzerName()).isEqualTo("test");
    }

    @Rollback
    @Test
    public void erstelleBenutzer_EqualEmail() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(b0);
        Assertions.assertThat(benutzerRepo.findAll().get(0).getBenutzerEmail()).isEqualTo("test@yahoo");
    }

    @Rollback
    @Test
    public void erstelleBenutzer_NameVorhanden() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(b0);

        Benutzer b1 = new Benutzer();
        b1.setBenutzerEmail("test@yahoo");
        b1.setBenutzerName("test");
        b1.setArtikel(new ArrayList<Artikel>());
        Benutzer test = benutzerM.erstelleBenutzer(b1);
        long ergebnis = test.getBenutzerId();
        assertEquals(-1, ergebnis);
    }

    @Rollback
    @Test
    public void bearbeiteBenutzer_EqualName() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(b0);
        Benutzer b1 = new Benutzer();
        b1.setBenutzerEmail("geaendert!");
        b1.setBenutzerName("test");
        b1.setArtikel(new ArrayList<Artikel>());
        Long bId = benutzerM.findBenutzerByName("test").getBenutzerId();
        benutzerM.bearbeiteBenutzer(bId, b1);
        Assertions.assertThat(benutzerRepo.findAll().get(0).getBenutzerEmail()).isEqualTo("geaendert!");
    }

    @Rollback
    @Test
    public void nameAlreadyExists_AlreadyExists() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerM.erstelleBenutzer(b0);
        Assertions.assertThat(benutzerM.nameSchonVorhanden("test")).isEqualTo(true);
    }

    @Rollback
    @Test
    public void nameAlreadyExists_IsNew() {
        Assertions.assertThat(benutzerM.nameSchonVorhanden("test")).isEqualTo(false);
    }

    @Rollback
    @Test
    public void sucheAusgehendeAnfragenTest() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        List ausleihen = new ArrayList<Ausleihe>();
        Ausleihe erste = new Ausleihe();
        erste.setAusleihStatus(Status.ANGEFRAGT);
        Ausleihe zweite = new Ausleihe();
        zweite.setAusleihStatus(Status.BESTAETIGT);
        ausleihen.add(erste);
        ausleihen.add(zweite);
        b0.setAusgeliehen(ausleihen);
        benutzerM.erstelleBenutzer(b0);

        assertEquals(1, benutzerM.sucheAusgehendeAnfragen(b0, Status.ANGEFRAGT).size());
        assertEquals(1, benutzerM.sucheAusgehendeAnfragen(b0, Status.BESTAETIGT).size());
        assertEquals(2, b0.getAusgeliehen().size());
    }

    @Rollback
    @Test
    public void sucheEingehendeAnfragenTest() {
        List ausleihen = new ArrayList<Ausleihe>();
        Ausleihe erste = new Ausleihe();
        erste.setAusleihStatus(Status.ANGEFRAGT);
        Ausleihe zweite = new Ausleihe();
        zweite.setAusleihStatus(Status.BESTAETIGT);
        ausleihen.add(erste);
        ausleihen.add(zweite);

        Artikel artikel = new Artikel();
        List artikelList = new ArrayList<Artikel>();
        artikelList.add(artikel);
        artikel.setAusgeliehen(ausleihen);
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(artikelList);

        benutzerM.erstelleBenutzer(b0);

        assertEquals(1, benutzerM.sucheEingehendeAnfragen(b0, Status.ANGEFRAGT).size());
        assertEquals(1, benutzerM.sucheEingehendeAnfragen(b0, Status.BESTAETIGT).size());
        assertEquals(2, artikel.getAusgeliehen().size());
    }
}
