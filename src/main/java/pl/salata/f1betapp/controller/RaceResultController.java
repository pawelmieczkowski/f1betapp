package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.RaceResultService;

import java.util.List;

@RestController
@RequestMapping("raceResult")
@AllArgsConstructor
public class RaceResultController {

    private final RaceResultService raceResultService;

    @GetMapping("/gp/{id}")
    public List<RaceResult> getByGrandPrixId(@PathVariable Long id) {
        return raceResultService.getByGrandPrixId(id);
    }
}
