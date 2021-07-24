package pl.salata.f1betapp.datapopulating;

import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.service.CircuitService;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class GrandPrixDataProcessor implements ItemProcessor<GrandPrixInput, GrandPrix> {

    private final CircuitService circuitService;

    public GrandPrixDataProcessor(CircuitService circuitService) {
        this.circuitService = circuitService;
    }

    @Override
    public GrandPrix process(GrandPrixInput input) throws Exception {
        GrandPrix grandPrix = new GrandPrix();

        grandPrix.setId(Long.parseLong(input.getRaceId()));
        grandPrix.setYear(input.getYear());
        grandPrix.setRound(Integer.parseInt(input.getRound()));

        Circuit circuit = circuitService.findById(input.getCircuitId());
        grandPrix.setCircuit(circuit);
        grandPrix.setLocalization(circuit.getLocation());
        grandPrix.setCountry(circuit.getCountry());

        grandPrix.setName(input.getName());

        try {
            grandPrix.setDate(LocalDate.parse(input.getDate(), ISO_LOCAL_DATE));
        } catch (DateTimeParseException e) {
            System.out.println("Date format not supported. - " + grandPrix.getName() + grandPrix.getYear() + e);
        }

        try {
            grandPrix.setTime(LocalTime.parse(input.getTime()));
        } catch (DateTimeParseException e) {
            System.out.println("Time format not supported. - " + grandPrix.getName() + grandPrix.getYear() + e);
        }

        grandPrix.setUrl(input.getUrl());

        return grandPrix;
    }
}
