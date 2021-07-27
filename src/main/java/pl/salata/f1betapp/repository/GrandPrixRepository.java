package pl.salata.f1betapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.salata.f1betapp.model.GrandPrix;

import java.util.List;
import java.util.Optional;

public interface GrandPrixRepository extends CrudRepository<GrandPrix, Long> {

    List<GrandPrix> findAllByYear(Integer year);

    @Query("SELECT g FROM grand_prix g JOIN FETCH g.raceResult JOIN FETCH g.circuit WHERE g.id = (:id)")
    Optional<GrandPrix> findByIdAndFetchRaceResults(Long id);

    @Query("SELECT g FROM grand_prix g JOIN FETCH g.qualificationResult JOIN FETCH g.circuit WHERE g.id = (:id)")
    Optional<GrandPrix> findByIdAndFetchQualificationResults(Long id);
}
