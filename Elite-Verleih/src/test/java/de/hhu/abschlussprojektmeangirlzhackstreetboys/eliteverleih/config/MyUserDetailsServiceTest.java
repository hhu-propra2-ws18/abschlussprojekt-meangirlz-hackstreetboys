package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.config;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess.BenutzerRepository;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MyUserDetailsServiceTest {



    BenutzerManager benutzerManager = mock(BenutzerManager.class);


    MyUserDetailsService service;


    @Test
    public void loadUserByUsernameTestOk() {

        service = new MyUserDetailsService(benutzerManager);
        Benutzer benutzer = new Benutzer();
        benutzer.setBenutzerEmail("test@test.test");
        benutzer.setBenutzerPasswort("1");
        benutzer.setBenutzerName("Jens");
        benutzer.setBenutzerRolle("ROLE_USER");

        when(benutzerManager.findBenutzerByName(anyString())).thenReturn(benutzer);
        UserDetails test = service.loadUserByUsername("Jens");
        assertNotNull(test);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameTestError(){
        service = new MyUserDetailsService(benutzerManager);
        when(benutzerManager.findBenutzerByName(anyString())).thenReturn(null);
        service.loadUserByUsername("Jens");
    }
}
