package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArtikelRepository extends CrudRepository<Artikel,Long> {
    List<Artikel> findAll();
    Artikel findArtikelByArtikelId(Long id);
}

