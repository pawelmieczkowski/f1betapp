package pl.salata.f1betapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.salata.f1betapp.model.Team;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);
}
