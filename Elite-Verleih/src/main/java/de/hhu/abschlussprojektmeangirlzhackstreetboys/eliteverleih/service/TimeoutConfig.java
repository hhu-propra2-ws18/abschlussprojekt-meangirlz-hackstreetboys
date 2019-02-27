package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class TimeoutConfig {

    @Bean
    public RestTemplate customRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(0);
        httpRequestFactory.setConnectTimeout(500);
        httpRequestFactory.setReadTimeout(0);

        return new RestTemplate(httpRequestFactory);
    }
}
