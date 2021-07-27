package pl.salata.f1betapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.salata.f1betapp.model.GrandPrix;

import java.util.List;

public interface GrandPrixRepository extends CrudRepository<GrandPrix, Long> {

    List<GrandPrix> findAllByYear(Integer year);
}
