package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Ausleihe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AusleiheRepository extends CrudRepository<Ausleihe,Long> {
        List<Ausleihe> findAll();
        Ausleihe findAusleiheByAusleihId(Long id);
}
