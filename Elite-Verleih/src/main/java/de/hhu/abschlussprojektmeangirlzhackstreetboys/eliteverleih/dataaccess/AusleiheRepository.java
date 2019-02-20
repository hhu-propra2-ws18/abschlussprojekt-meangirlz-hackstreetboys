package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Ausleihe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AusleiheRepository extends CrudRepository<Ausleihe,Long> {
        List<Ausleihe> findAll();
        Ausleihe findAusleiheByAusleihId(Long id);
}
