package pl.salata.f1betapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.salata.f1betapp.model.Driver;

public interface DriverRepository extends CrudRepository<Driver, Long> {
}