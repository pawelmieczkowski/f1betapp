package pl.salata.f1betapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.salata.f1betapp.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {

}
