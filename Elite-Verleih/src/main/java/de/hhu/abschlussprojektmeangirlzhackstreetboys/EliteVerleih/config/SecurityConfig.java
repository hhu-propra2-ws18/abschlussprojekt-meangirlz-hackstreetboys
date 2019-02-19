package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable().authorizeRequests()
                .antMatchers("/registrieren").permitAll()
                .antMatchers("/*").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/Uebersicht?id=1",true)
                .failureUrl("/login?error=true")
                .permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("Jens").password("{noop}password").roles("USER");
    }
}
