package pl.salata.f1betapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.RaceFinishStatus;
import pl.salata.f1betapp.repository.RaceFinishStatusRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaceFinishStatusServiceTest {

    @Mock
    private RaceFinishStatusRepository raceFinishStatusRepository;

    @InjectMocks
    private RaceFinishStatusService raceFinishStatusService;

    @Test
    void shouldThrowExceptionIfEntityNotFound() {
        //given
        when(raceFinishStatusRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(EntityNotFoundException.class, () -> raceFinishStatusService.findById(5L),
                "RaceFinishStatus was not found for parameter 5L");
    }

    @Test
    void shouldReturnRaceFinishStatus() {
        //given
        final long RESULT_ID = 5L;
        RaceFinishStatus raceFinishStatus = new RaceFinishStatus();
        raceFinishStatus.setId(RESULT_ID);
        when(raceFinishStatusRepository.findById(anyLong())).thenReturn(Optional.of(raceFinishStatus));
        //when
        RaceFinishStatus raceFinishStatusFromService = raceFinishStatusService.findById(RESULT_ID);
        //then
        assertEquals(RESULT_ID, raceFinishStatusFromService.getId());
    }
}