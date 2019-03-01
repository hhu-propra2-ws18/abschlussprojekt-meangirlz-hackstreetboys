package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.ArtikelRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.AusleiheRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto.ReservationDto;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("dev")
public class AusleiheManagerTest {


    static PropayManager propayManager = mock(PropayManager.class);

    AusleiheManager ausleiheM;

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    ArtikelRepository artikelRepo;

    @Autowired
    AusleiheRepository ausleiheRepo;

    /*@Autowired
    PropayManager propayManager;*/

    private Calendar sD0 = new GregorianCalendar(2019, 1, 8);
    private Calendar eD0 = new GregorianCalendar(2019, 1, 10);

    @BeforeClass
    public static void setupBeforeClass() {
        ReservationDto mockReservation = new ReservationDto();
        mockReservation.setAmount(1.0);
        mockReservation.setId(100);
        when(propayManager.kautionReserviern(anyString(), anyString(), anyInt())).thenReturn(mockReservation);
    }

    @Before
    public void setup() {
        ausleiheM = new AusleiheManager(ausleiheRepo, propayManager, artikelRepo, benutzerRepo);

    }

    private Long erstelleBeispiel(Calendar start, Calendar ende) {
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
        artikel.setArtikelKaution(3);
        artikel.setArtikelName("Hammer");
        artikel.setArtikelOrt("Werkstatt");
        artikel.setArtikelTarif(1);
        artikel.setBenutzer(bBesitzer);
        artikel = artikelRepo.save(artikel);
        bBesitzer.getArtikel().add(artikel);
        benutzerRepo.save(bBesitzer);

        Ausleihe ausleihe = new Ausleihe();
        ausleihe.setBenutzer(bAusleiher);
        ausleihe.setArtikel(artikelRepo.findArtikelByArtikelId(artikel.getArtikelId()));
        ausleihe.setAusleihRueckgabedatum(ende);
        ausleihe.setAusleihStartdatum(start);
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
        ausleiheM.erstelleAusleihe(alId.get(0), alId.get(2), sD0, eD0);
        Assertions.assertThat(artikelRepo.findArtikelByArtikelId(alId.get(2)).getAusgeliehen()).hasSize(1);
    }

    @Rollback
    @Test
    public void erstelleAusleihe_Mappings_inBesitzer() {
        ArrayList<Long> alId = konfiguriereErstelleBeispiel();
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
        Long ausleiheId = erstelleBeispiel(sD0, eD0);
        Ausleihe ausleihe = ausleiheM.bearbeiteAusleihe(ausleiheId, Status.AKTIV);
        Assertions.assertThat(ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus()).isEqualTo(Status.AKTIV);
    }

    @Rollback
    @Test
    public void bearbeiteAusleihe_Mappings_AusleiheInAusleiher() {
        Long ausleiheId = erstelleBeispiel(sD0, eD0);
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
        Long ausleiheId = erstelleBeispiel(sD0, eD0);
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
        Long ausleiheId = erstelleBeispiel(sD0, eD0);
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
        Long ausleiheId = erstelleBeispiel(sD0, eD0);

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
        Long ausleiheId = erstelleBeispiel(sD0, eD0);

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
        Long ausleiheId = erstelleBeispiel(sD0, eD0);

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
        Long ausleiheId = erstelleBeispiel(sD0, eD0);

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

    @Rollback
    @Test
    public void bestaetigeAusleihe_Akzeptieren() {

        Calendar heute = new GregorianCalendar();
        Calendar morgen = new GregorianCalendar();
        morgen.add(Calendar.DATE, 1);
        long ausleiheId = erstelleBeispiel(heute, morgen);

        assertEquals(Status.ANGEFRAGT, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
        ausleiheM.bestaetigeAusleihe(ausleiheId);
        assertEquals(Status.BESTAETIGT, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
    }

    @Rollback
    @Test
    public void bestaetigeAusleihe_loescheKollidierende() {
        Calendar heute = new GregorianCalendar();
        Calendar morgen = new GregorianCalendar();
        morgen.add(Calendar.DATE, 1);
        long ausleiheId = erstelleBeispiel(heute, morgen);
        Artikel artikel = ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getArtikel();

        Benutzer bAusleiher2 = new Benutzer();
        bAusleiher2.setBenutzerEmail("test@yahoo");
        bAusleiher2.setBenutzerName("Ausleiher2");
        bAusleiher2.setAusgeliehen(new ArrayList<Ausleihe>());
        bAusleiher2 = benutzerRepo.save(bAusleiher2);

        Ausleihe ausleihe2 = new Ausleihe();
        ausleihe2.setBenutzer(bAusleiher2);
        ausleihe2.setArtikel(artikel);
        ausleihe2.setAusleihRueckgabedatum(morgen);
        ausleihe2.setAusleihStartdatum(heute);
        ausleihe2.setAusleihStatus(Status.ANGEFRAGT);
        ausleihe2 = ausleiheRepo.save(ausleihe2);
        bAusleiher2.setAusgeliehen(new ArrayList<Ausleihe>());
        bAusleiher2.getAusgeliehen().add(ausleihe2);
        benutzerRepo.save(bAusleiher2);
        artikel.getAusgeliehen().add(ausleihe2);
        artikelRepo.save(artikel);

        assertEquals(2, artikelRepo.findArtikelByArtikelId(artikel.getArtikelId()).getAusgeliehen().size());
        ausleiheM.bestaetigeAusleihe(ausleiheId);
        assertEquals(1, artikelRepo.findArtikelByArtikelId(artikel.getArtikelId()).getAusgeliehen().size());
    }

    @Rollback
    @Test
    public void bestaetigeAusleihe_UngueltigesDatum() {

        Calendar altesDatum = new GregorianCalendar(2018, 1, 1);
        Calendar morgen = new GregorianCalendar();
        morgen.add(Calendar.DATE, 1);
        long ausleiheId = erstelleBeispiel(altesDatum, morgen);

        assertEquals(Status.ANGEFRAGT, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
        ausleiheM.bestaetigeAusleihe(ausleiheId);
        assertEquals(1, ausleiheRepo.findAll().size());
        assertEquals(Status.ABGELEHNT, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
    }

    @Rollback
    @Test
    public void bestaetigeAusleihe_KeinGeld() {
        PropayManager minusMockManager = mock(PropayManager.class);
        ReservationDto minusMockReservation = new ReservationDto();
        minusMockReservation.setAmount(1.0);
        minusMockReservation.setId(-1);

        when(minusMockManager.kautionReserviern(anyString(), anyString(), anyInt())).thenReturn(minusMockReservation);
        AusleiheManager ausleiheManager = new AusleiheManager(ausleiheRepo, minusMockManager, artikelRepo, benutzerRepo);

        Calendar heute = new GregorianCalendar();
        Calendar morgen = new GregorianCalendar();
        morgen.add(Calendar.DATE, 1);
        long ausleiheId = erstelleBeispiel(heute, morgen);

        assertEquals(Status.ANGEFRAGT, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
        ausleiheManager.bestaetigeAusleihe(ausleiheId);
        assertEquals(Status.ABGELEHNT, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
    }

    @Rollback
    @Test
    public void istAusgeliehenTest() {
        Calendar gestern = new GregorianCalendar();
        gestern.add(Calendar.DATE, -1);
        Calendar heute = new GregorianCalendar();
        Calendar morgen = new GregorianCalendar();
        morgen.add(Calendar.DATE, 1);
        Calendar ueberuebermorgen = new GregorianCalendar();
        ueberuebermorgen.add(Calendar.DATE, 3);
        Calendar nachEndDatum = new GregorianCalendar();
        nachEndDatum.add(Calendar.DATE, 4);
        Calendar nachEndDatumPlus = new GregorianCalendar();
        nachEndDatumPlus.add(Calendar.DATE, 5);
        long ausleiheId = erstelleBeispiel(heute, ueberuebermorgen);
        ausleiheM.bearbeiteAusleihe(ausleiheId, Status.BESTAETIGT);
        long artikelId = ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getArtikel().getArtikelId();

        assertEquals(true, ausleiheM.istAusgeliehen(artikelId, new GregorianCalendar(), morgen));
        assertEquals(true, ausleiheM.istAusgeliehen(artikelId, gestern, morgen));
        assertEquals(true, ausleiheM.istAusgeliehen(artikelId, gestern, nachEndDatum));
        assertEquals(true, ausleiheM.istAusgeliehen(artikelId, heute, ueberuebermorgen));
        assertEquals(true, ausleiheM.istAusgeliehen(artikelId, morgen, nachEndDatum));
        assertEquals(false, ausleiheM.istAusgeliehen(artikelId, nachEndDatum, nachEndDatumPlus));
    }

    @Rollback
    @Test
    public void getKonflikteTest() {
        Calendar heute = new GregorianCalendar();
        Calendar morgen = new GregorianCalendar();
        morgen.add(Calendar.DATE, 1);
        long ausleiheId = erstelleBeispiel(heute, morgen);
        Artikel artikel = ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getArtikel();

        Benutzer bAusleiher2 = new Benutzer();
        bAusleiher2.setBenutzerEmail("test@yahoo");
        bAusleiher2.setBenutzerName("Ausleiher2");
        bAusleiher2.setAusgeliehen(new ArrayList<Ausleihe>());
        bAusleiher2 = benutzerRepo.save(bAusleiher2);

        Ausleihe ausleihe2 = new Ausleihe();
        ausleihe2.setBenutzer(bAusleiher2);
        ausleihe2.setArtikel(artikel);
        ausleihe2.setAusleihRueckgabedatum(morgen);
        ausleihe2.setAusleihStartdatum(heute);
        ausleihe2.setAusleihStatus(Status.KONFLIKT);
        ausleihe2 = ausleiheRepo.save(ausleihe2);
        bAusleiher2.setAusgeliehen(new ArrayList<Ausleihe>());
        bAusleiher2.getAusgeliehen().add(ausleihe2);
        benutzerRepo.save(bAusleiher2);
        artikel.getAusgeliehen().add(ausleihe2);
        artikelRepo.save(artikel);

        assertEquals(2, ausleiheRepo.findAll().size());
        List<Ausleihe> ergebnis = ausleiheM.getKonflike(ausleiheRepo.findAll());
        assertEquals(1, ergebnis.size());
        assertEquals(Status.KONFLIKT, ergebnis.get(0).getAusleihStatus());
    }

    @Rollback
    @Test
    public void Ausleihe_zurueckgebenOK() {

        when(propayManager.ueberweisen(anyString(), anyString(), anyInt())).thenReturn(200);
        Calendar gestern = new GregorianCalendar();
        gestern.add(Calendar.DATE, -1);
        Calendar morgen = new GregorianCalendar();
        morgen.add(Calendar.DATE, 1);
        long ausleiheId = erstelleBeispiel(gestern, morgen);

        assertEquals(Status.ANGEFRAGT, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
        ausleiheM.zurueckGeben(ausleiheId);
        assertEquals(Status.ABGEGEBEN, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
    }

    @Rollback
    @Test
    public void Ausleihe_zurueckgebenFehler() {

        when(propayManager.ueberweisen(anyString(), anyString(), anyInt())).thenReturn(400);
        Calendar gestern = new GregorianCalendar();
        gestern.add(Calendar.DATE, -1);
        Calendar morgen = new GregorianCalendar();
        morgen.add(Calendar.DATE, 1);
        long ausleiheId = erstelleBeispiel(gestern, morgen);

        assertEquals(Status.ANGEFRAGT, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
        assertEquals(400, ausleiheM.zurueckGeben(ausleiheId));
        assertEquals(Status.ANGEFRAGT, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
    }

    @Rollback
    @Test
    public void Ausleihe_rueckgabeAkzeptieren() {

        when(propayManager.kautionFreigeben(anyString(), anyInt())).thenReturn(200);
        Calendar gestern = new GregorianCalendar();
        gestern.add(Calendar.DATE, -1);
        Calendar morgen = new GregorianCalendar();
        morgen.add(Calendar.DATE, 1);
        long ausleiheId = erstelleBeispiel(gestern, morgen);
        ausleiheM.bearbeiteAusleihe(ausleiheId, Status.ABGEGEBEN);

        assertEquals(Status.ABGEGEBEN, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
        ausleiheM.rueckgabeAkzeptieren(ausleiheId);
        assertEquals(Status.BEENDET, ausleiheRepo.findAusleiheByAusleihId(ausleiheId).getAusleihStatus());
    }
}