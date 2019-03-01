package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class DataInitializer implements ServletContextInitializer {

    @Autowired
    BenutzerManager benutzerM;

    @Autowired
    BenutzerRepository benutzerRepository;

    private String supportPasswort = "1";

    @Override
    public void onStartup(ServletContext servletContext)
        throws ServletException {
        if (benutzerM.findBenutzerByName("support") == null) {
            System.out.println("Populating the database");
            Benutzer support = new Benutzer();
            support.setBenutzerEmail("support@hhu.de");
            support.setBenutzerName("support");
            support.setArtikel(new ArrayList<Artikel>());
            support.setBenutzerPasswort(supportPasswort);
            support.setBenutzerRolle("ROLE_SUPPORT");
            benutzerRepository.save(support);
        }
    }
}
