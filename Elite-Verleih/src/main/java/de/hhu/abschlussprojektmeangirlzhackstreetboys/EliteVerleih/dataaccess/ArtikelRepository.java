package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dataaccess;

import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Artikel;
import de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.modell.Benutzer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ArtikelRepository extends CrudRepository<Artikel,Long> {
    List<Artikel> findAll();
    Artikel findArtikelByArtikelId(Long id);
    Optional<Artikel> findArtikelByArtikelName(String artikelName);
}

