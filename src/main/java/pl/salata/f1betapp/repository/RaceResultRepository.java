package pl.salata.f1betapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.salata.f1betapp.model.RaceResult;

import java.util.List;

public interface RaceResultRepository extends CrudRepository<RaceResult, Long> {
    List<RaceResult> findAllByGrandPrixId(Long id);

    List<RaceResult> findByGrandPrixIdAndFinishingPosition(Long id, String finishingPosition);

    @Query("SELECT r \n" +
            "FROM race_result r \n" +
            "INNER JOIN FETCH r.grandPrix \n" +
            "WHERE r.driverId = (:id)")
    List<RaceResult> findByDriver(Long id);
}
