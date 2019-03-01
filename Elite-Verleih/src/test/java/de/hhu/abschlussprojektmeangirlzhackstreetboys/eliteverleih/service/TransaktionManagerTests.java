package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.AusleiheRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.TransaktionRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Import( {TransaktionManager.class})
@RunWith(SpringRunner.class)
@DataJpaTest
public class TransaktionManagerTests {

    @Autowired
    TransaktionManager transM;

    @Autowired
    TransaktionRepository transRepo;
    @Autowired
    ArtikelRepository artikelRepo;
    @Autowired
    AusleiheRepository ausleiheRepo;
    @Autowired
    BenutzerRepository benutzerRepo;

    //change: 0=Angefragt,1=Abgegeben,3=verkaufAnstelleAusleihe(returnsArtikelId)
    private Long erstelleBeispiel(Calendar start, Calendar ende, int change) {
        Benutzer bAusleiher = new Benutzer();
        bAusleiher.setBenutzerEmail("ausleiher@yahoo");
        bAusleiher.setBenutzerName("Ausleiher");
        bAusleiher.setAusgeliehen(new ArrayList<Ausleihe>());
        bAusleiher = benutzerRepo.save(bAusleiher);
        Benutzer bBesitzer = new Benutzer();
        bBesitzer.setBenutzerEmail("besitzer@yahoo");
        bBesitzer.setBenutzerName("Besitzer");
        bBesitzer.setArtikel(new ArrayList<Artikel>());
        bBesitzer = benutzerRepo.save(bBesitzer);
        Artikel artikel = new Artikel();
        artikel.setArtikelBeschreibung("beschreibung");
        artikel.setArtikelName("Hammer");
        artikel.setArtikelOrt("Werkstatt");
        artikel.setBenutzer(bBesitzer);
        if (change < 3) {
            artikel.setArtikelKaution(3);
            artikel.setArtikelTarif(1);
        } else {
            artikel.setArtikelPreis(2);
        }
        artikel = artikelRepo.save(artikel);
        bBesitzer.getArtikel().add(artikel);
        benutzerRepo.save(bBesitzer);
        if (change > 2) {
            return artikel.getArtikelId();
        } else {
            Ausleihe ausleihe = new Ausleihe();
            ausleihe.setBenutzer(bAusleiher);
            ausleihe.setArtikel(artikelRepo.findArtikelByArtikelId(artikel.getArtikelId()));
            ausleihe.setAusleihRueckgabedatum(ende);
            ausleihe.setAusleihStartdatum(start);
            if (change == 0) {
                ausleihe.setAusleihStatus(Status.ANGEFRAGT);
            } else {
                ausleihe.setAusleihStatus(Status.ABGEGEBEN);
            }
            ausleihe = ausleiheRepo.save(ausleihe);
            bAusleiher.setAusgeliehen(new ArrayList<Ausleihe>());
            bAusleiher.getAusgeliehen().add(ausleihe);
            benutzerRepo.save(bAusleiher);
            artikel.setAusgeliehen(new ArrayList<Ausleihe>());
            artikel.getAusgeliehen().add(ausleihe);
            artikelRepo.save(artikel);
            return ausleihe.getAusleihId();
        }
    }

    private Long erstelleValidesVerleiheBeispiel() {
        Calendar startDatum = new GregorianCalendar();
        startDatum.add(Calendar.DATE, -1);
        Calendar rueckgabeDatum = new GregorianCalendar(2019, 1, 10);
        Long ausleiheId = erstelleBeispiel(startDatum, rueckgabeDatum, 1);
        return ausleiheId;
    }

    private Long erstelleValidesVerkaufBeispiel() {
        Calendar startDatum = new GregorianCalendar();
        startDatum.add(Calendar.DATE, -1);
        Calendar rueckgabeDatum = new GregorianCalendar(2019, 1, 10);
        Long artikelId = erstelleBeispiel(startDatum, rueckgabeDatum, 3);
        return artikelId;
    }

    @Rollback
    @Test
    public void setzteTransaktionsBetragTest_StatusAngefragt() {
        Calendar startDatum = new GregorianCalendar(2019, 1, 8);
        Calendar rueckgabeDatum = new GregorianCalendar(2019, 1, 10);
        Long ausleiheId = erstelleBeispiel(startDatum, rueckgabeDatum, 0);
        Assertions.assertThat(transM.setzeTransaktionBetrag(ausleiheId)).isEqualTo(0);
    }

    @Rollback
    @Test
    public void setzteTransaktionsBetragTest_StatusAbgegeben_AberZuFrueh() {
        Calendar startDatum = new GregorianCalendar();
        startDatum.add(Calendar.DATE, +1);
        Calendar rueckgabeDatum = new GregorianCalendar(2019, 1, 10);
        Long ausleiheId = erstelleBeispiel(startDatum, rueckgabeDatum, 1);
        Assertions.assertThat(transM.setzeTransaktionBetrag(ausleiheId)).isEqualTo(0);
    }

    @Rollback
    @Test
    public void setzteTransaktionsBetragTest_StatusAbgegeben() {
        Long ausleiheId = erstelleValidesVerleiheBeispiel();
        Assertions.assertThat(transM.setzeTransaktionBetrag(ausleiheId)).isNotEqualTo(0);
    }

    @Rollback
    @Test
    public void erstelleTransaktionTest_BesitzerGesetzt() {
        Long ausleiheId = erstelleValidesVerleiheBeispiel();
        Long besitzerId = benutzerRepo.findBenutzerByBenutzerName("Besitzer").get().getBenutzerId();
        Transaktion trans = transM.erstelleTransaktion(ausleiheId);
        Assertions.assertThat(benutzerRepo.findBenutzerByBenutzerId(besitzerId).getTransaktionen()).contains(trans);
    }

    @Rollback
    @Test
    public void erstelleTransaktionTest_AusleiherGesetzt() {
        Long ausleiheId = erstelleValidesVerleiheBeispiel();
        Long ausleiherId = benutzerRepo.findBenutzerByBenutzerName("Ausleiher").get().getBenutzerId();
        Transaktion trans = transM.erstelleTransaktion(ausleiheId);
        Assertions.assertThat(benutzerRepo.findBenutzerByBenutzerId(ausleiherId).getTransaktionen()).contains(trans);
    }

    @Rollback
    @Test
    public void erstelleTransaktionKautionTest_BesitzerGesetzt() {
        Long ausleiheId = erstelleValidesVerleiheBeispiel();
        Long besitzerId = benutzerRepo.findBenutzerByBenutzerName("Besitzer").get().getBenutzerId();
        Transaktion trans = transM.erstelleTransaktionKaution(ausleiheId);
        Assertions.assertThat(benutzerRepo.findBenutzerByBenutzerId(besitzerId).getTransaktionen()).contains(trans);
    }

    @Rollback
    @Test
    public void erstelleTransaktionKautionTest_AusleiherGesetzt() {
        Long ausleiheId = erstelleValidesVerleiheBeispiel();
        Long ausleiherId = benutzerRepo.findBenutzerByBenutzerName("Ausleiher").get().getBenutzerId();
        Transaktion trans = transM.erstelleTransaktionKaution(ausleiheId);
        Assertions.assertThat(benutzerRepo.findBenutzerByBenutzerId(ausleiherId).getTransaktionen()).contains(trans);
    }

    @Rollback
    @Test
    public void erstelleTransaktionVerkaufTest_BesitzerGesetzt() {
        Long artikelId = erstelleValidesVerkaufBeispiel();
        Long besitzerId = benutzerRepo.findBenutzerByBenutzerName("Besitzer").get().getBenutzerId();
        Transaktion trans = transM.erstelleTransaktionVerkauf(artikelId, benutzerRepo.findBenutzerByBenutzerId(besitzerId).getBenutzerName());
        Assertions.assertThat(benutzerRepo.findBenutzerByBenutzerId(besitzerId).getTransaktionen()).contains(trans);
    }

    @Rollback
    @Test
    public void erstelleTransaktionVerkaufTest_AusleiherGesetzt() {
        Long artikelId = erstelleValidesVerkaufBeispiel();
        Long ausleiherId = benutzerRepo.findBenutzerByBenutzerName("Ausleiher").get().getBenutzerId();
        Transaktion trans = transM.erstelleTransaktionVerkauf(artikelId, benutzerRepo.findBenutzerByBenutzerId(ausleiherId).getBenutzerName());
        Assertions.assertThat(benutzerRepo.findBenutzerByBenutzerId(ausleiherId).getTransaktionen()).contains(trans);
    }

    @Rollback
    @Test
    public void erstelleTransaktionTest_KorrekteTransaktion() {
        Long ausleihId = erstelleValidesVerleiheBeispiel();
        Transaktion trans = transM.erstelleTransaktion(ausleihId);
        Assertions.assertThat(trans.getBesitzerName()).isEqualTo(ausleiheRepo.findAusleiheByAusleihId(ausleihId).getArtikel().getBenutzer().getBenutzerName());
        Assertions.assertThat(trans.getKundeName()).isEqualTo(ausleiheRepo.findAusleiheByAusleihId(ausleihId).getBenutzer().getBenutzerName());
        Assertions.assertThat(trans.getArtikelName()).isEqualTo(ausleiheRepo.findAusleiheByAusleihId(ausleihId).getArtikel().getArtikelName());
    }

    @Rollback
    @Test
    public void erstelleTransaktionKautionTest_KorrekteTransaktion() {
        Long ausleihId = erstelleValidesVerleiheBeispiel();
        Transaktion trans = transM.erstelleTransaktionKaution(ausleihId);
        Assertions.assertThat(trans.getBesitzerName()).isEqualTo(ausleiheRepo.findAusleiheByAusleihId(ausleihId).getArtikel().getBenutzer().getBenutzerName());
        Assertions.assertThat(trans.getKundeName()).isEqualTo(ausleiheRepo.findAusleiheByAusleihId(ausleihId).getBenutzer().getBenutzerName());
        Assertions.assertThat(trans.getArtikelName()).isEqualTo(ausleiheRepo.findAusleiheByAusleihId(ausleihId).getArtikel().getArtikelName() + " - KAUTION");
    }

    @Rollback
    @Test
    public void erstelleTransaktionVerkaufTest_KorrekteTransaktion() {
        Long artikelId = erstelleValidesVerkaufBeispiel();
        Long kaeuferId = benutzerRepo.findBenutzerByBenutzerName("Ausleiher").get().getBenutzerId();
        Transaktion trans = transM.erstelleTransaktionVerkauf(artikelId, benutzerRepo.findBenutzerByBenutzerName("Ausleiher").get().getBenutzerName());
        Assertions.assertThat(trans.getBesitzerName()).isEqualTo(artikelRepo.findArtikelByArtikelId(artikelId).getBenutzer().getBenutzerName());
        Assertions.assertThat(trans.getKundeName()).isEqualTo(benutzerRepo.findBenutzerByBenutzerId(kaeuferId).getBenutzerName());
        Assertions.assertThat(trans.getArtikelName()).isEqualTo(artikelRepo.findArtikelByArtikelId(artikelId).getArtikelName());
    }
}
