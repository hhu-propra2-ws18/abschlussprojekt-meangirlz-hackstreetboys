package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.config;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.service.BenutzerManager;
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
                        "{noop}password", enabled, accountNonExpired,
                        credentialsNonExpired, accountNonLocked,
                        getAuthority("ROLE_USER"));
    }

    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
    private static List<GrantedAuthority> getAuthority (String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}