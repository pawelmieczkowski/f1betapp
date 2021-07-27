package pl.salata.f1betapp.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.salata.f1betapp.service.DataProcessingService;


@RestController
@AllArgsConstructor
@RequestMapping("data")
public class DataProcessingController {

    private final DataProcessingService dataProcessingService;
    
    @PostMapping("circuits")
    public String populateCircuits(){
        return dataProcessingService.populateCircuits();
    }

    @PostMapping("grandsPrix")
    public String populateGrandPrix() {
        return dataProcessingService.populateGrandPrix();
    }

    @PostMapping("status")
    public String populateRaceFinishStatus() {
        return dataProcessingService.populateRaceFinishStatus();
    }

    @PostMapping("drivers")
    public String populateDriver() {
        return dataProcessingService.populateDriver();
    }

    @PostMapping("teams")
    public String populateTeam() {
        return dataProcessingService.populateTeam();
    }

    @PostMapping("raceResults")
    public String populateRaceResults() {
        return dataProcessingService.populateRaceResults();
    }

    @PostMapping("qualificationResults")
    public String populateQualificationResults() {
        return dataProcessingService.populateQualificationResults();
    }
}
