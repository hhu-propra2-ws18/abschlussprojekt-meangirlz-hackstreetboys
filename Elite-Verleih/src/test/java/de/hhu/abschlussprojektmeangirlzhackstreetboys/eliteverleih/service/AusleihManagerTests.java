package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

public class AusleihManagerTests {
/*
    @Autowired
    AusleiheManager ausleiheM;

    @Autowired
    BenutzerRepository benutzerRepo;

    @Autowired
    BenutzerRepository artikelRepo;

    @Autowired
    AusleiheRepository ausleiheRepo;

    @Rollback
    @Test
    public void createArtikel_EqualName(){
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
        artikelM.erstelleArtikel(bId,a0);
        Assertions.assertThat(artikelM.getAllArtikel().get(0).getArtikelName().equals("Hammer"));
    }

    @Rollback
    @Test
    public void isAusleiheTest () {
        Ausleihe aus = new Ausleihe();
        Date start = new Date();
        Date end = new Date();
        try {
            start = new SimpleDateFormat( "yyyy-mm-dd" ).parse("2019-03-11");
            end = new SimpleDateFormat( "yyyy-mm-dd" ).parse("2019-03-29");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        aus.setAusleihStartdatum(new Date(new SimpleDateFormat("yyyy-mm-dd").parse("2019-03-20")));
        aus.setAusleihRueckgabedatum(new Date(new SimpleDateFormat("yyyy-mm-dd").parse("2019-03-22")));
        ausleiheRepo.save(aus);
        Assertions.assertThat(ausleiheM.isAusgeliehen(23, start, end))
    }*/
}
