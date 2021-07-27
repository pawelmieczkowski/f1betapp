package pl.salata.f1betapp.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.service.GrandPrixService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("grands-prix")
public class GrandPrixController {

    GrandPrixService grandPrixService;

    @GetMapping()
    public List<GrandPrix> getAllByYear(@RequestParam Integer year) {
        return grandPrixService.getAllByYear(year);
    }

    @GetMapping("{id}")
    public MappingJacksonValue getById(@PathVariable Long id) {
        GrandPrix grandPrix = grandPrixService.getById(id);
        return filter(grandPrix, "qualificationResult", "raceResult");
    }

    @GetMapping("{id}/results")
    public MappingJacksonValue getByIdWithRaceResults(@PathVariable Long id) {
        GrandPrix grandPrix = grandPrixService.getByIdWithRaceResults(id);
        return filter(grandPrix, "qualificationResult");
    }

    @GetMapping("{id}/qualifications")
    public MappingJacksonValue getByIdWithQualificationResults(@PathVariable Long id) {
        GrandPrix grandPrix = grandPrixService.getByIdWithQualificationResults(id);
        return filter(grandPrix, "raceResult");
    }

    private MappingJacksonValue filter(GrandPrix grandPrix, String... propertyArray) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(propertyArray);
        FilterProvider filters = new SimpleFilterProvider().addFilter("GrandPrixFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(grandPrix);
        mapping.setFilters(filters);

        return mapping;
    }
}
