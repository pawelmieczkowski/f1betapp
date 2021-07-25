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

        InputProcessor.parseNumber(input.getRaceId(), Long.class).ifPresent(grandPrix::setId);
        InputProcessor.parseNumber(input.getRound(), Integer.class).ifPresent(grandPrix::setRound);

        grandPrix.setYear(InputProcessor.validateString(input.getYear()));
        grandPrix.setName(InputProcessor.validateString(input.getName()));
        grandPrix.setUrl(InputProcessor.validateString(input.getUrl()));

        InputProcessor.parseNumber(input.getCircuitId(), Long.class)
                .ifPresent(value -> {
                    Circuit circuit = circuitService.findById(value);
                    grandPrix.setCircuit(circuit);
                    grandPrix.setLocalization(circuit.getLocation());
                    grandPrix.setCountry(circuit.getCountry());
                });

        grandPrix.setDate(InputProcessor.parseDate(input.getDate()));
        grandPrix.setTime(InputProcessor.parseTime(input.getTime()));

        return grandPrix;
    }
}
