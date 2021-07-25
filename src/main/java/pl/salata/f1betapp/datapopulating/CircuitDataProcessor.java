package pl.salata.f1betapp.datapopulating;

import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.Circuit;

public class CircuitDataProcessor implements ItemProcessor<CircuitInput, Circuit> {

    @Override
    public Circuit process(CircuitInput input) throws Exception {
        Circuit circuit = new Circuit();

        InputProcessor.parseNumber(input.getCircuitId(), Long.class).ifPresent(circuit::setId);

        circuit.setName(InputProcessor.validateString(input.getName()));
        circuit.setLocation(InputProcessor.validateString(input.getLocation()));
        circuit.setCountry(InputProcessor.validateString(input.getCountry()));
        circuit.setUrl(InputProcessor.validateString(input.getUrl()));

        circuit.setLatitude(InputProcessor.validateString(input.getLatitude()));
        circuit.setLongitude(InputProcessor.validateString(input.getLongitude()));
        circuit.setAltitude(InputProcessor.validateString(input.getAltitude()));

        return circuit;
    }
}
