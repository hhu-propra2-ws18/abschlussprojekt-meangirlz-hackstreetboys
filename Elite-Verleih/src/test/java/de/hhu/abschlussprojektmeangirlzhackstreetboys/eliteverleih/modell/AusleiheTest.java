package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AusleiheTest {

    Calendar nachEndeDatum = new GregorianCalendar(2019,1,28);
    Calendar vorEndeDatum = new GregorianCalendar(2019,1,19);
    Calendar vorStartDatum = new GregorianCalendar(2019, 1, 10);


    @Test
    public void AnzahlUeberzogenTest(){
        Ausleihe ausleihe = erstelleTestAusleihe();
        int zuspaet = ausleihe.getAnzahlUeberzogen(nachEndeDatum);
        int vorEnde= ausleihe.getAnzahlUeberzogen(vorEndeDatum);
        int vorStart = ausleihe.getAnzahlUeberzogen(vorStartDatum);
        assertEquals(8, zuspaet);
        assertEquals(0, vorEnde);
        assertEquals(0, vorStart);
    }

    @Test
    public void berechneKostenTest(){
        Ausleihe ausleihe = erstelleTestAusleihe();
        int zuspaet = ausleihe.berechneKosten(nachEndeDatum);
        int vorEnde= ausleihe.berechneKosten(vorEndeDatum);
        int vorStart = ausleihe.berechneKosten(vorStartDatum);
        assertEquals(13, zuspaet);
        assertEquals(2, vorEnde);
        assertEquals(0, vorStart);
    }

    @Test
    public void ausleiheGueltigesDatumTest(){
        Ausleihe ausleihe = erstelleTestAusleihe();
        boolean nachStart = ausleihe.gueltigesDatum(vorEndeDatum);
        boolean nachEnde = ausleihe.gueltigesDatum(nachEndeDatum);
        boolean vorStart = ausleihe.gueltigesDatum(vorStartDatum);
        assertFalse(nachEnde);
        assertFalse(nachStart);
        assertFalse(!vorStart);
    }

    private Ausleihe erstelleTestAusleihe(){
        Calendar start = new GregorianCalendar();
        start.set(2019, 1, 18);
        Calendar ende = new GregorianCalendar();
        ende.set(2019, 1, 20);
        Artikel artikel = new Artikel();
        artikel.setArtikelTarif(1);
        Ausleihe ausleihe = new Ausleihe(artikel, start, ende, new Benutzer(),
            Status.BESTAETIGT, 1);
        return ausleihe;
    }
}
