package pl.salata.f1betapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.repository.CircuitRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CircuitServiceTest {

    @Mock
    private CircuitRepository circuitRepository;

    @InjectMocks
    private CircuitService circuitService;

    @Test
    void shouldThrowExceptionIfEntityNotFound() {
        //given
        when(circuitRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(EntityNotFoundException.class, () -> circuitService.findById(1L),
                "Circuit was not found for parameter 1");
    }

    @Test
    void shouldReturnCircuit() {
        //given
        final long CIRCUIT_ID = 2L;
        Circuit circuit = new Circuit();
        circuit.setId(CIRCUIT_ID);
        when(circuitRepository.findById(CIRCUIT_ID)).thenReturn(Optional.of(circuit));
        //when
        Circuit circuitFromService = circuitService.findById(CIRCUIT_ID);
        //then
        assertEquals(CIRCUIT_ID, circuitFromService.getId());
    }
}