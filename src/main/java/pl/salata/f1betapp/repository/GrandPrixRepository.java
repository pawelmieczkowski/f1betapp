package pl.salata.f1betapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.salata.f1betapp.model.GrandPrix;

import java.util.List;
import java.util.Optional;

import static org.hibernate.loader.Loader.SELECT;

public interface GrandPrixRepository extends CrudRepository<GrandPrix, Long> {

    @Query("SELECT g FROM grand_prix g JOIN FETCH g.circuit WHERE g.year = (:year)")
    List<GrandPrix> findAllByYear(Integer year);

    @Query("SELECT g FROM grand_prix g LEFT JOIN FETCH g.raceResult JOIN FETCH g.circuit WHERE g.id = (:id)")
    Optional<GrandPrix> findByIdAndFetchRaceResults(Long id);

    @Query("SELECT g FROM grand_prix g LEFT JOIN FETCH g.qualificationResult JOIN FETCH g.circuit WHERE g.id = (:id)")
    Optional<GrandPrix> findByIdAndFetchQualificationResults(Long id);

    @Query("SELECT DISTINCT g.year from grand_prix g")
    List<Long> findAllYears();

    List<GrandPrix> findByCircuitId(Long id);
}
