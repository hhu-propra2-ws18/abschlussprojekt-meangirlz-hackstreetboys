package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Benutzer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BenutzerRepository extends CrudRepository<Benutzer,Long> {
    List<Benutzer> findAll();
    Benutzer findBenutzerByBenutzerId(Long benutzerId);
}
