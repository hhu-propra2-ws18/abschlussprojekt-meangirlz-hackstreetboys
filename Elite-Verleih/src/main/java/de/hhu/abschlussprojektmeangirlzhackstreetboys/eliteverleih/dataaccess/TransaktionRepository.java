package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dataaccess;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell.Transaktion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransaktionRepository extends CrudRepository<Transaktion, Long> {
    List<Transaktion> findAll();

    Transaktion findTransaktionByTransaktionId(Long id);
}
