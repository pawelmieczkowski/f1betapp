package pl.salata.f1betapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.salata.f1betapp.model.Team;

import java.util.Optional;

public interface TeamRepository extends CrudRepository<Team, Long> {

    Optional<Team> findByName(String name);
}
