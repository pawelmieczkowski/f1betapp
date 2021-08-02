package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.service.DriverService;

@AllArgsConstructor
@RequestMapping("drivers")
@RestController
@CrossOrigin
public class DriverController {
    private final DriverService driverService;

    @GetMapping("{id}")
    public Driver getDriver(@PathVariable Long id){
        return driverService.getById(id);
    }

}
