package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.repository.DriverRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver getById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Driver.class, String.valueOf(id)));
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getByNameAndDateOfBirth(String forename, String surname, LocalDate dateOfBirth) {
        return driverRepository.getByForenameAndSurnameAndDateOfBirth(forename, surname, dateOfBirth)
                .orElseThrow(() -> new EntityNotFoundException(Driver.class, "FORENAME = " + forename +
                        " SURNAME = " + surname + "DATE OF BIRTH = " + dateOfBirth));
    }
}
