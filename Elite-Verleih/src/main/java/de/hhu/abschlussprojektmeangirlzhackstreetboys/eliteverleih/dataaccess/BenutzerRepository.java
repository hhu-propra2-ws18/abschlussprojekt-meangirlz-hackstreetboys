package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Benutzer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BenutzerRepository extends CrudRepository<Benutzer, Long> {
    List<Benutzer> findAll();

    Benutzer findBenutzerByBenutzerId(Long benutzerId);

    Optional<Benutzer> findBenutzerByBenutzerName(String benutzerName);

}
