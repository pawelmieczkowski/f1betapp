package pl.salata.f1betapp.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.RaceResultService;

import java.util.List;

@RestController
@RequestMapping("race-result")
@AllArgsConstructor
public class RaceResultController {

    private final RaceResultService raceResultService;

    @GetMapping()
    public MappingJacksonValue getByGrandPrixId(@RequestParam Long grandPrix) {
        List<RaceResult> results = raceResultService.getByGrandPrixId(grandPrix);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("grandPrix");
        FilterProvider filters = new SimpleFilterProvider().addFilter("GrandPrixFilter", filter)
                .addFilter("RaceResultFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(results);
        mapping.setFilters(filters);
        return mapping;

    }

    @GetMapping("/driver")
    public MappingJacksonValue getDriverResults(@RequestParam Long id) {
        List<RaceResult> results = raceResultService.getDriverResults(id);
        return filter(results,  "raceResult", "qualificationResult", "circuit");
    }

    private MappingJacksonValue filter(List<RaceResult> results, String... propertyArray) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(propertyArray);
        FilterProvider filters = new SimpleFilterProvider().addFilter("GrandPrixFilter", filter)
                .addFilter("RaceResultFilter", SimpleBeanPropertyFilter.serializeAll());
        MappingJacksonValue mapping = new MappingJacksonValue(results);
        mapping.setFilters(filters);
        return mapping;
    }

}
