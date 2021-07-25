package pl.salata.f1betapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.salata.f1betapp.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
}
