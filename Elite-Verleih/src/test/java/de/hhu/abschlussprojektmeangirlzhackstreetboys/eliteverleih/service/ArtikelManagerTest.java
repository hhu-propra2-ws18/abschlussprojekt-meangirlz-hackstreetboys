package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Import( {ArtikelManager.class})
@RunWith(SpringRunner.class)
@DataJpaTest
public class ArtikelManagerTest {

    @Autowired
    ArtikelManager artikelM;

    @Autowired
    BenutzerRepository benutzerRepo;

    @Rollback
    @Test
    public void erstelleArtikel_EqualName() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerRepo.save(b0);
        Long bId = benutzerRepo.findBenutzerByBenutzerName("test").get().getBenutzerId();
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        artikelM.erstelleVerleihen(bId, a0);
        Assertions.assertThat(artikelM.getAllArtikel().get(0).getArtikelName().equals("Hammer"));
    }

    @Rollback
    @Test
    public void bearbeiteArtikel_AndereName() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerRepo.save(b0);
        Long bId = benutzerRepo.findBenutzerByBenutzerName("test").get().getBenutzerId();
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        artikelM.erstelleVerleihen(bId, a0);
        Artikel a1 = new Artikel();
        a1.setArtikelBeschreibung("beschreibung");
        a1.setArtikelKaution(3);
        a1.setArtikelName("Hammer2");
        a1.setArtikelOrt("Werkstatt");
        a1.setArtikelTarif(1);
        artikelM.bearbeiteArtikel(artikelM.getAllArtikel().get(0).getArtikelId(), a1);
        Assertions.assertThat(artikelM.getAllArtikel().get(0).getArtikelName()).isEqualTo("Hammer2");
    }

    @Rollback
    @Test
    public void bearbeiteArtikel_AndereBeschreibung() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerRepo.save(b0);
        Long bId = benutzerRepo.findBenutzerByBenutzerName("test").get().getBenutzerId();
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        artikelM.erstelleVerleihen(bId, a0);
        Artikel a1 = new Artikel();
        a1.setArtikelBeschreibung("test");
        a1.setArtikelKaution(3);
        a1.setArtikelName("Hammer2");
        a1.setArtikelOrt("Werkstatt");
        a1.setArtikelTarif(1);
        artikelM.bearbeiteArtikel(artikelM.getAllArtikel().get(0).getArtikelId(), a1);
        Assertions.assertThat(artikelM.getAllArtikel().get(0).getArtikelBeschreibung().equals("test"));
    }

    @Rollback
    @Test
    public void loescheArtikel_nichtAusgeliehen() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerRepo.save(b0);
        Long bId = benutzerRepo.findBenutzerByBenutzerName("test").get().getBenutzerId();
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        a0.setAusgeliehen(new ArrayList<Ausleihe>());
        artikelM.erstelleVerleihen(bId, a0);
        Artikel a1 = new Artikel();
        a1.setArtikelBeschreibung("test");
        a1.setArtikelKaution(3);
        a1.setArtikelName("Hammer2");
        a1.setArtikelOrt("Werkstatt");
        a1.setArtikelTarif(1);
        a1.setAusgeliehen(new ArrayList<Ausleihe>());
        artikelM.erstelleVerleihen(bId, a1);
        assertEquals(2, artikelM.getAllArtikel().size());
        artikelM.loescheArtikel(artikelM.getAllArtikel().get(0).getArtikelId());
        assertEquals(1, artikelM.getAllArtikel().size());
    }

    @Rollback
    @Test
    public void loescheArtikel_ausgeliehen() {
        Benutzer b0 = new Benutzer();
        b0.setBenutzerEmail("test@yahoo");
        b0.setBenutzerName("test");
        b0.setArtikel(new ArrayList<Artikel>());
        benutzerRepo.save(b0);
        Benutzer b1 = new Benutzer();
        b1.setBenutzerEmail("test@yahoo");
        b1.setBenutzerName("test");
        b1.setArtikel(new ArrayList<Artikel>());
        benutzerRepo.save(b1);
        Long bId = benutzerRepo.findBenutzerByBenutzerName("test").get().getBenutzerId();
        Artikel a0 = new Artikel();
        a0.setArtikelBeschreibung("beschreibung");
        a0.setArtikelKaution(3);
        a0.setArtikelName("Hammer");
        a0.setArtikelOrt("Werkstatt");
        a0.setArtikelTarif(1);
        a0.setAusgeliehen(new ArrayList<Ausleihe>());
        artikelM.erstelleVerleihen(bId, a0);
        Artikel a1 = new Artikel();
        a1.setArtikelBeschreibung("test");
        a1.setArtikelKaution(3);
        a1.setArtikelName("Hammer2");
        a1.setArtikelOrt("Werkstatt");
        a1.setArtikelTarif(1);
        Ausleihe ausleihe = new Ausleihe();
        ausleihe.setAusleihStatus(Status.BESTAETIGT);
        ausleihe.setArtikel(a1);
        ausleihe.setBenutzer(b1);

        List<Ausleihe> ausleiheList = new ArrayList<Ausleihe>();
        ausleiheList.add(ausleihe);
        a1.setAusgeliehen(ausleiheList);
        artikelM.erstelleVerleihen(bId, a1);
        assertEquals(2, artikelM.getAllArtikel().size());
        artikelM.loescheArtikel(artikelM.getAllArtikel().get(0).getArtikelId());
        assertEquals(1, artikelM.getAllArtikel().size());
    }
}