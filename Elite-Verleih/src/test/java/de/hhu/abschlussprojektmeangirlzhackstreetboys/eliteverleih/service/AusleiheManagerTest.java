package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.AusleiheRepository;
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
import java.util.Arrays;
import java.util.Date;

@Import( {AusleiheManager.class})
@RunWith(SpringRunner.class)
@DataJpaTest
public class AusleiheManagerTest {

    @Autowired
    AusleiheManager ausleiheM;

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    ArtikelRepository artikelRepo;

    @Autowired
    AusleiheRepository ausleiheRepo;

    private Long erstelleBeispiel() {
        Benutzer bAusleiher = new Benutzer();
        bAusleiher.setBenutzerEmail("test@yahoo");
        bAusleiher.setBenutzerName("Ausleiher");
        bAusleiher.setAusgeliehen(new ArrayList<Ausleihe>());
        bAusleiher = benutzerRepo.save(bAusleiher);
        Benutzer bBesitzer = new Benutzer();
        bBesitzer.setBenutzerEmail("test@yahoo");
        bBesitzer.setBenutzerName("Besitzer");
        bBesitzer.setArtikel(new ArrayList<Artikel>());
        bBesitzer = benutzerRepo.save(bBesitzer);
        Artikel artikel = new Artikel();
        artikel.setArtikelBeschreibung("beschreibung");
        artikel.setArtikelKaution(3);
        artikel.setArtikelName("Hammer");
        artikel.setArtikelOrt("Werkstatt");
        artikel.setArtikelTarif(1);
        artikel.setBenutzer(bBesitzer);
        artikel = artikelRepo.save(artikel);
        bBesitzer.getArtikel().add(artikel);
        bBesitzer = benutzerRepo.save(bBesitzer);
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);

        Ausleihe ausleihe = new Ausleihe();
        ausleihe.setBenutzer(bAusleiher);
        ausleihe.setArtikel(artikel);
        ausleihe.setAusleihRueckgabedatum(eD0);
        ausleihe.setAusleihStartdatum(sD0);
        ausleihe.setAusleihStatus(Status.ANGEFRAGT);
        ausleihe = ausleiheRepo.save(ausleihe);
        bAusleiher.setAusgeliehen(new ArrayList<Ausleihe>());
        bAusleiher.getAusgeliehen().add(ausleihe);
        benutzerRepo.save(bAusleiher);
        artikel.setAusgeliehen(new ArrayList<Ausleihe>());
        artikel.getAusgeliehen().add(ausleihe);
        artikelRepo.save(artikel);
        return ausleihe.getAusleihId();
    }

    private ArrayList<Long> konfiguriereErstelleBeispiel() {
        Benutzer bAusleiher = new Benutzer();
        bAusleiher.setBenutzerEmail("test@yahoo");
        bAusleiher.setBenutzerName("Ausleiher");
        bAusleiher.setAusgeliehen(new ArrayList<Ausleihe>());
        bAusleiher = benutzerRepo.save(bAusleiher);
        Benutzer bBesitzer = new Benutzer();
        bBesitzer.setBenutzerEmail("test@yahoo");
        bBesitzer.setBenutzerName("Besitzer");
        bBesitzer.setArtikel(new ArrayList<Artikel>());
        bBesitzer = benutzerRepo.save(bBesitzer);
        Artikel artikel = new Artikel();
        artikel.setArtikelBeschreibung("beschreibung");
        artikel.setArtikelKaution(3);
        artikel.setArtikelName("Hammer");
        artikel.setArtikelOrt("Werkstatt");
        artikel.setArtikelTarif(1);
        artikel.setBenutzer(bBesitzer);
        artikel = artikelRepo.save(artikel);
        bBesitzer.getArtikel().add(artikel);
        bBesitzer = benutzerRepo.save(bBesitzer);

        ArrayList<Long> alId = new ArrayList<>();
        alId.addAll(Arrays.asList(bAusleiher.getBenutzerId(),   //0
            bBesitzer.getBenutzerId(),                      //1
            artikel.getArtikelId()));                       //2
        return alId;
    }

    @Rollback
    @Test
    public void erstelleAusleihe_RichtigErstellt() {
        ArrayList<Long> alId = konfiguriereErstelleBeispiel();
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        ausleiheM.erstelleAusleihe(alId.get(0), alId.get(2), sD0, eD0);
        Assertions.assertThat(artikelRepo.findArtikelByArtikelId(alId.get(2)).getAusgeliehen()).hasSize(1);
    }

    @Rollback
    @Test
    public void erstelleAusleihe_Mappings_inBesitzer() {
        ArrayList<Long> alId = konfiguriereErstelleBeispiel();
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        ausleiheM.erstelleAusleihe(alId.get(0), alId.get(2), sD0, eD0);
        Benutzer besitzer = benutzerRepo.findBenutzerByBenutzerId(alId.get(1));
        if (besitzer.getArtikel() != null) {
            if (besitzer.getArtikel().get(0).getAusgeliehen() != null) {
                Assertions.assertThat(besitzer.getArtikel().get(0).getAusgeliehen()).hasSize(1);
            } else {
                Assertions.fail("besitzer.getArtikel0 = null");
            }
        } else {
            Assertions.fail("besitzer.artikel = null");
        }
    }

    @Rollback
    @Test
    public void erstelleAusleihe_Mappings_inAusleiher() {
        ArrayList<Long> alId = konfiguriereErstelleBeispiel();
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        ausleiheM.erstelleAusleihe(alId.get(0), alId.get(2), sD0, eD0);
        Benutzer ausleiher = benutzerRepo.findBenutzerByBenutzerId(alId.get(0));
        if (ausleiher.getAusgeliehen() != null) {
            Assertions.assertThat(ausleiher.getAusgeliehen()).hasSize(1);
        } else {
            Assertions.fail("ausleiher.ausgeliehen = null");
        }
    }

    @Rollback
    @Test
    public void erstelleAusleihe_Mappings_inArtikel() {
        ArrayList<Long> alId = konfiguriereErstelleBeispiel();
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        ausleiheM.erstelleAusleihe(alId.get(0), alId.get(2), sD0, eD0);
        Artikel artikel = artikelRepo.findArtikelByArtikelId(alId.get(2));
        if (artikel.getAusgeliehen() != null) {
            Assertions.assertThat(artikel.getAusgeliehen()).hasSize(1);
        } else {
            Assertions.fail("ausleiher.ausgeliehen = null");
        }
    }

    @Rollback
    @Test
    public void bearbeiteAusleihe_RichtigGeaendert() {
        Long ausleiheId = erstelleBeispiel();
        Ausleihe ausleihe = ausleiheM.bearbeiteAusleihe(ausleiheId, Status.AKTIV);
        Assertions.assertThat(ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus()).isEqualTo(Status.AKTIV);
    }

    /*@Rollback
    @Test
    public void bearbeiteAusleihe_sollteKlappen(){
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
        b1 = benutzerRepo.save(b1);
        Date sD0 = new Date(2019, 5, 8);
        Date eD0 = new Date(2019, 5, 10);
        Ausleihe ausleihe = ausleiheM.erstelleAusleihe(b0.getBenutzerId(), a0.getArtikelId(), sD0, eD0);

        ausleihe = ausleiheM.bearbeiteAusleihe(ausleihe.getAusleihId(), Status.AKTIV);
        ausleihe.setAusleihStatus(Status.ABGELEHNT);
        if(b0.getAusgeliehen() != null)
            Assertions.assertThat(b0.getAusgeliehen().get(0).getAusleihStatus())
                    .isEqualTo(Status.AKTIV);
        else
            Assertions.fail("BenutzerAusleiher.getAusgeliehen war null");
    }
*/
    @Rollback
    @Test
    public void bearbeiteAusleihe_Mappings_AusleiheInAusleiher() {
        Long ausleiheId = erstelleBeispiel();
        Ausleihe ausleihe = ausleiheM.bearbeiteAusleihe(ausleiheId, Status.AKTIV);

        Benutzer newInstance = benutzerRepo.findBenutzerByBenutzerName("Ausleiher").get();
        if (newInstance.getAusgeliehen() != null) {
            Assertions.assertThat(newInstance.getAusgeliehen()).hasSize(1);
            Assertions.assertThat(newInstance.getAusgeliehen().get(0).getAusleihStatus()).isEqualTo(Status.AKTIV);
        } else {
            Assertions.fail("BenutzerAusleiher.getAusgeliehen war null");
        }
    }

    @Rollback
    @Test
    public void bearbeiteAusleihe_Mappings_AusleiheInArtikel() {
        Long ausleiheId = erstelleBeispiel();
        Ausleihe ausleihe = ausleiheM.bearbeiteAusleihe(ausleiheId, Status.AKTIV);
        Artikel a = artikelRepo.findAll().get(0);
        if (a.getAusgeliehen() != null) {
            Assertions.assertThat(a.getAusgeliehen()).hasSize(1);
            Assertions.assertThat(a.getAusgeliehen().get(0).getAusleihStatus()).isEqualTo(Status.AKTIV);
        } else {
            Assertions.fail("Artikel.getAusgeliehen war null");
        }
    }

    @Rollback
    @Test
    public void bearbeiteAusleihe_Mappings_AusleiheInBesitzer() {
        Long ausleiheId = erstelleBeispiel();
        Ausleihe ausleihe = ausleiheM.bearbeiteAusleihe(ausleiheId, Status.AKTIV);
        Benutzer besitzer = benutzerRepo.findBenutzerByBenutzerName("Besitzer").get();
        for (Artikel a : besitzer.getArtikel()) {
            if (a.getAusgeliehen() != null) {
                Assertions.assertThat(a.getAusgeliehen()).hasSize(1);
                Assertions.assertThat(a.getAusgeliehen().get(0).getAusleihStatus()).isEqualTo(Status.AKTIV);
            } else {
                Assertions.fail("BenutzerBesitzer.getAusgeliehen war null");
            }
        }
    }

    @Rollback
    @Test
    public void loescheAusleihe_AusleiheGeloescht() {
        Long ausleiheId = erstelleBeispiel();

        ausleiheM.loescheAusleihe(ausleiheId);

        if (ausleiheRepo.findAll() != null) {
            Assertions.assertThat(ausleiheRepo.findAll().size()).isEqualTo(0);
        } else {
            Assertions.fail("ausleiher.getAusgeliehen == null");
        }
    }

    @Rollback
    @Test
    public void loescheAusleihe_Mappings_AusleiheInAusleiher() {
        Long ausleiheId = erstelleBeispiel();

        ausleiheM.loescheAusleihe(ausleiheId);
        Benutzer newInstance = benutzerRepo.findBenutzerByBenutzerName("Ausleiher").get();

        if (newInstance.getAusgeliehen() != null) {
            Assertions.assertThat(newInstance.getAusgeliehen().size()).isEqualTo(0);
        } else {
            Assertions.fail("ausleiher.getAusgeliehen == null");
        }
    }

    @Rollback
    @Test
    public void loescheAusleihe_Mappings_AusleiheInArtikel() {
        Long ausleiheId = erstelleBeispiel();

        ausleiheM.loescheAusleihe(ausleiheId);
        Artikel a = artikelRepo.findAll().get(0);

        if (a.getAusgeliehen() != null) {
            Assertions.assertThat(a.getAusgeliehen().size()).isEqualTo(0);
        } else {
            Assertions.fail("a.getAusgeliehen == null");
        }
    }

    @Rollback
    @Test
    public void loescheAusleihe_Mappings_AusleiheInBesitzer() {
        Long ausleiheId = erstelleBeispiel();

        ausleiheM.loescheAusleihe(ausleiheId);
        Benutzer newInstance = benutzerRepo.findBenutzerByBenutzerName("Besitzer").get();

        if (newInstance.getArtikel() != null) {
            if (newInstance.getArtikel().get(0).getAusgeliehen() != null) {
                Assertions.assertThat(newInstance.getArtikel().get(0).getAusgeliehen().size()).isEqualTo(0);
            } else {
                Assertions.fail("besitzer.getArtikel0.getAusgeliehen == null");
            }
        } else {
            Assertions.fail("besitzer.getArtikel == null");
        }
    }
}