package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.service.DriverService;

import java.util.List;

@AllArgsConstructor
@RequestMapping("drivers")
@RestController
public class DriverController {
    private final DriverService driverService;

    @GetMapping("{id}")
    public Driver getDriver(@PathVariable Long id){
        return driverService.getById(id);
    }

    @GetMapping("all")
    public List<Driver> getAllDrivers(){
        return driverService.getAllDrivers();
    }
}
