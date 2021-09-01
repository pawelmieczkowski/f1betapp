package pl.salata.f1betapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.repository.DriverRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    @Test
    void shouldThrowExceptionIfEntityNotFound() {
        //given
        when(driverRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(EntityNotFoundException.class, () -> driverService.getById(1L),
                "Driver was not found for parameter 1");
    }

    @Test
    void shouldReturnDriver() {
        //given
        final long DRIVER_ID = 2L;
        Driver driver = new Driver();
        driver.setId(DRIVER_ID);
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));
        //when
        Driver driverFromService = driverService.getById(DRIVER_ID);
        //then
        assertEquals(DRIVER_ID, driverFromService.getId());
    }
}