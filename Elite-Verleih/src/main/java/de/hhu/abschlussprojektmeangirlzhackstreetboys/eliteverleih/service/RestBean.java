package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestBean {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
