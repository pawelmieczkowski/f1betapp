package pl.salata.f1betapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.repository.RaceResultRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaceResultServiceTest {

    @Mock
    private RaceResultRepository raceResultRepository;

    @InjectMocks
    private RaceResultService raceResultService;

    @Test
    void shouldThrowExceptionIfListEmptyForGetByGrandPrixId() {
        //given
        when(raceResultRepository.findAllByGrandPrixId(anyLong())).thenReturn(new ArrayList<>());
        //then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> raceResultService.getByGrandPrixId(5L));
        assertEquals("RaceResult was not found for parameter GRAND PRIX ID = 5", exception.getMessage());
    }

    @Test
    void shouldReturnRaceResultsForGetByGrandPrixId() {
        //given
        List<RaceResult> results = new ArrayList<>();
        results.add(new RaceResult());
        results.add(new RaceResult());
        when(raceResultRepository.findAllByGrandPrixId(anyLong())).thenReturn(results);
        //when
        List<RaceResult> resultsFromService = raceResultService.getByGrandPrixId(1L);
        //then
        assertEquals(2, resultsFromService.size());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionForGetWinnerByGrandPrixId() {
        //given
        when(raceResultRepository.findByGrandPrixIdAndFinishingPosition(anyLong(), matches("1"))).thenReturn(new ArrayList<>());
        //then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> raceResultService.getWinnerByGrandPrixId(5L));
        assertEquals("String was not found for parameter GRAND PRIX ID = 5", exception.getMessage());
    }

    @Test
    void shouldReturnNamesForGetWinnerByGrandPrixId() {
        //given
        List<RaceResult> results = new ArrayList<>();
        RaceResult raceResult1 = new RaceResult();
        raceResult1.setDriverName("Driver1");
        results.add(raceResult1);
        RaceResult raceResult2 = new RaceResult();
        raceResult2.setDriverName("Driver2");
        results.add(raceResult2);
        when(raceResultRepository.findByGrandPrixIdAndFinishingPosition(anyLong(), matches("1"))).thenReturn(results);
        //when
        List<String> namesFromService = raceResultService.getWinnerByGrandPrixId(5L);
        //then
        assertEquals(2, namesFromService.size());
        assertEquals("Driver2", namesFromService.get(1));
    }

    @Test
    void shouldThrowEntityNotFountExceptionForGetDriverResults() {
        //given
        when(raceResultRepository.findByDriver(anyLong())).thenReturn(new ArrayList<>());
        //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> raceResultService.getDriverResults(1L));
        assertEquals("RaceResult was not found for parameter 1", exception.getMessage());
    }

    @Test
    void shouldReturnRaceResultsForGetDriverResults() {
        //given
        List<RaceResult> results = new ArrayList<>();
        results.add(new RaceResult());
        results.add(new RaceResult());
        when(raceResultRepository.findByDriver(anyLong())).thenReturn(results);
        //when
        List<RaceResult> resultsFromService = raceResultService.getDriverResults(1L);
        //then
        assertEquals(2, resultsFromService.size());
    }

    @Test
    void shouldThrowEntityNotFountExceptionForGetTeamResults() {
        //given
        when(raceResultRepository.findByTeam(anyString(), anyInt())).thenReturn(new ArrayList<>());
        //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> raceResultService.getTeamResults("TeamName1", 2009));
        assertEquals("RaceResult was not found for parameter TEAM = TeamName1 YEAR = 2009", exception.getMessage());
    }

    @Test
    void shouldReturnRaceResultsForGetTeamResults() {
        //given
        List<RaceResult> results = new ArrayList<>();
        results.add(new RaceResult());
        results.add(new RaceResult());
        when(raceResultRepository.findByTeam(anyString(), anyInt())).thenReturn(results);
        //when
        List<RaceResult> resultsFromService = raceResultService.getTeamResults("TeamName2", 2010);
        //then
        assertEquals(2, resultsFromService.size());
    }

    @Test
    void shouldThrowEntityNotFountExceptionForGetAllYearsByTeam() {
        //given
        when(raceResultRepository.findAllYearsByTeam(anyString())).thenReturn(new ArrayList<>());
        //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> raceResultService.getAllYearsByTeam("TeamName1"));
        assertEquals("Long was not found for parameter TeamName1", exception.getMessage());
    }

    @Test
    void shouldReturnRaceResultsForGetAllYearsByTeam() {
        //given
        List<Long> results = new ArrayList<>();
        results.add(2001L);
        results.add(2010L);
        when(raceResultRepository.findAllYearsByTeam(anyString())).thenReturn(results);
        //when
        List<Long> resultsFromService = raceResultService.getAllYearsByTeam("TeamName2");
        //then
        assertEquals(2, resultsFromService.size());
    }
}