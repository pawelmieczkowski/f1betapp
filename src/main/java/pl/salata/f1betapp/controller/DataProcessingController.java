package pl.salata.f1betapp.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.salata.f1betapp.service.DataProcessingService;


@RestController
@RequestMapping("dataProcessing")
public class DataProcessingController {

    private final DataProcessingService dataProcessingService;

    public DataProcessingController(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }

    @PostMapping("/circuits")
    public String populateCircuits(){
        return dataProcessingService.populateCircuits();
    }

    @PostMapping("/grandPrix")
    public String populateGrandPrix() {
        return dataProcessingService.populateGrandPrix();
    }
}
