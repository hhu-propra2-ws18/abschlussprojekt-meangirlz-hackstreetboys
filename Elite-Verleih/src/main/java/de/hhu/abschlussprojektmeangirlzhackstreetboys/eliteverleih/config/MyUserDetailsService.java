package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.config;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service.BenutzerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    BenutzerManager benutzerManager;

    public UserDetails loadUserByUsername(String Benutzername)
            throws UsernameNotFoundException {

        Benutzer benutzer = benutzerManager.findBenutzerByName(Benutzername);
        if (benutzer == null) {
            throw new UsernameNotFoundException(
                    "No user found with username: "+ Benutzername);
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return  new org.springframework.security.core.userdetails.User
                (benutzer.getBenutzerName(),
                        "{noop}"+ benutzer.getBenutzerPasswort(), enabled, accountNonExpired,
                        credentialsNonExpired, accountNonLocked,
                        getAuthority(benutzer.getBenutzerRolle()));
    }
    private static List<GrantedAuthority> getAuthority (String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}