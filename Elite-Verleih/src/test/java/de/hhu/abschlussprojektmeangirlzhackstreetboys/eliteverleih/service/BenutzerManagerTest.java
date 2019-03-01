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
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    @Rollback
    @Test
    public void findeVerspaeteteAusleihenTest() {

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
        b0.setAusgeliehen(ausleihen);
        benutzerM.erstelleBenutzer(b0);
        List<Ausleihe> leereAusleihe = new ArrayList<Ausleihe>();

        assertEquals(leereAusleihe, benutzerM.findeVerspaeteteAusleihe(b0));

        Benutzer b1 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test1");
        b0.setArtikel(new ArrayList<Artikel>());
        Calendar start = new GregorianCalendar();
        start.set(2019, 1, 22);
        Calendar rueckgabe = new GregorianCalendar();
        rueckgabe.set(2019, 1, 23);
        zweite.setAusleihStartdatum(start);
        zweite.setAusleihRueckgabedatum(rueckgabe);
        ausleihen.add(zweite);
        b1.setAusgeliehen(ausleihen);
        benutzerM.erstelleBenutzer(b1);

        assertEquals(1, benutzerM.findeVerspaeteteAusleihe(b1).size());

        Benutzer b2 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test1");
        b0.setArtikel(new ArrayList<Artikel>());

        Ausleihe dritte = new Ausleihe();
        zweite.setAusleihStatus(Status.BESTAETIGT);

        Calendar start1 = new GregorianCalendar();
        start.set(2019, 2, 22);
        Calendar rueckgabe1 = new GregorianCalendar();
        rueckgabe1.add(Calendar.DATE, 1);
        dritte.setAusleihStartdatum(start1);
        dritte.setAusleihRueckgabedatum(rueckgabe1);
        List<Ausleihe> ausleihen2 = new ArrayList<Ausleihe>();
        ausleihen2.add(erste);
        ausleihen2.add(dritte);

        b2.setAusgeliehen(ausleihen2);
        benutzerM.erstelleBenutzer(b2);

        assertEquals(0, benutzerM.findeVerspaeteteAusleihe(b2).size());

    }
}
