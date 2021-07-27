package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.service.GrandPrixService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("grandPrix")
public class GrandPrixController {

    GrandPrixService grandPrixService;

    @GetMapping("/year/{year}")
    public List<GrandPrix> getAllByYear(@PathVariable Integer year) {
        return grandPrixService.findAllByYear(year);
    }

    @GetMapping("/id/{id}")
    public GrandPrix getById(@PathVariable Long id) {
        return grandPrixService.findById(id);
    }
}
