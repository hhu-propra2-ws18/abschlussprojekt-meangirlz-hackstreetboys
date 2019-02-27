package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class EliteVerleihApplication {

    public static void main(String[] args) {
        SpringApplication.run(EliteVerleihApplication.class, args);
    }

}

