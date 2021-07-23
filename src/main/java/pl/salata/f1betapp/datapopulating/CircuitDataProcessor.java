package pl.salata.f1betapp.datapopulating;

import org.springframework.batch.item.ItemProcessor;
import pl.salata.f1betapp.model.Circuit;

public class CircuitDataProcessor implements ItemProcessor<CircuitInput, Circuit> {

    @Override
    public Circuit process(CircuitInput input) throws Exception {
        Circuit circuit = new Circuit();

        circuit.setId(Long.parseLong(input.getCircuitId()));
        circuit.setName(input.getName());
        circuit.setLocation(input.getLocation());
        circuit.setCountry(input.getCountry());

        circuit.setLatitude(input.getLatitude());
        circuit.setLongitude(input.getLongitude());
        circuit.setAltitude(input.getAltitude());

        circuit.setUrl(input.getUrl());

        return circuit;
    }
}
