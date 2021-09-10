package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.service.DriverService;

import java.util.List;

@AllArgsConstructor
@RequestMapping("api/drivers")
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
