package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.repository.DriverRepository;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver getById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(("Invalid id provided")));
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }
}
