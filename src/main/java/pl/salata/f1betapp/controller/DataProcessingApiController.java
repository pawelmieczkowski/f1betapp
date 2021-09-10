package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.DataProcessingApiService;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/data-recent")
public class DataProcessingApiController {

    private final DataProcessingApiService dataProcessingApiService;

    @PostMapping("race-result")
    public List<RaceResult> populateRaceResult(@RequestParam Integer year, @RequestParam Integer round) throws IOException {
        return dataProcessingApiService.populateRaceResult(year, round);
    }

    @PostMapping("qualification-result")
    public List<QualificationResult> populateQualificationResult(@RequestParam Integer year, @RequestParam Integer round) throws IOException {
        return dataProcessingApiService.populateQualificationResult(year, round);
    }
}
