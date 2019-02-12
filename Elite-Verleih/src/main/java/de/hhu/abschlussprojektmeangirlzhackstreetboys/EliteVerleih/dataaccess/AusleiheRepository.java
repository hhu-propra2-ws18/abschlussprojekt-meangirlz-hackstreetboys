package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Ausleihe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AusleiheRepository extends CrudRepository<Ausleihe,Long> {
        List<Ausleihe> findAll();
}
