package pl.salata.f1betapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.salata.f1betapp.model.Driver;

import java.time.LocalDate;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> getByForenameAndSurnameAndDateOfBirth(String forename, String surname, LocalDate dateOfBirth);
}
