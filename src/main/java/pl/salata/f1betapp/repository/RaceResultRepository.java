package pl.salata.f1betapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.salata.f1betapp.model.RaceResult;

import java.util.List;

public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {
    List<RaceResult> findAllByGrandPrixId(Long id);

    List<RaceResult> findByGrandPrixIdAndFinishingPosition(Long id, String finishingPosition);

    @Query("SELECT r \n" +
            "FROM race_result r \n" +
            "INNER JOIN FETCH r.grandPrix \n" +
            "WHERE r.driverId = (:id)")
    List<RaceResult> findByDriver(Long id);

    @Query("SELECT r \n" +
            "FROM race_result r \n" +
            "INNER JOIN FETCH r.grandPrix \n" +
            "WHERE r.teamName = (:name)\n" +
            "AND r.grandPrix.year = (:year)")
    List<RaceResult> findByTeam(String name, Integer year);

    @Query("SELECT DISTINCT r.grandPrix.year\n" +
            "FROM race_result r\n" +
            "WHERE r.teamName = (:name)")
    List<Long> findAllYearsByTeam(String name);

    RaceResult findTopByOrderByIdDesc();
}
