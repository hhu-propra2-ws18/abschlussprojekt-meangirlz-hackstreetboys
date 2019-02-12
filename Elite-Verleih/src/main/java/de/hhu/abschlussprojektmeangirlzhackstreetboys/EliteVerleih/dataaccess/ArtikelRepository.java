package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess.Modell.Artikel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtikelRepository extends CrudRepository<Artikel,Long> {
    List<Artikel> findAll();
}

