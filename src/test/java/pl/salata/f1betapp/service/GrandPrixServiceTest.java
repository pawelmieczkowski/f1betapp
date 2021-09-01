package pl.salata.f1betapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.repository.GrandPrixRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrandPrixServiceTest {

    @Mock
    private GrandPrixRepository grandPrixRepository;

    @InjectMocks
    private GrandPrixService grandPrixService;

    @Test
    void shouldThrowExceptionIfEntityNotFoundForGetById() {
        //given
        when(grandPrixRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(EntityNotFoundException.class, () -> grandPrixService.getById(2L),
                "GrandPrix was not found for parameter 2");
    }

    @Test
    void shouldReturnGrandPrixForGetById() {
        //given
        final long GP_ID = 3L;
        GrandPrix grandPrix = new GrandPrix();
        grandPrix.setId(GP_ID);
        when(grandPrixRepository.findById(GP_ID)).thenReturn(Optional.of(grandPrix));
        //when
        GrandPrix grandPrixFromService = grandPrixService.getById(GP_ID);
        //then
        assertEquals(GP_ID, grandPrixFromService.getId());
    }

    @Test
    void shouldThrowExceptionIfEntityNotFoundForGetByIdWithRaceResults() {
        //given
        when(grandPrixRepository.findByIdAndFetchRaceResults(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(EntityNotFoundException.class, () -> grandPrixService.getByIdWithRaceResults(2L),
                "GrandPrix was not found for parameter 2");
    }

    @Test
    void shouldReturnGrandPrixForGetByIdWithRaceResults() {
        //given
        final long GP_ID = 3L;
        GrandPrix grandPrix = new GrandPrix();
        grandPrix.setId(GP_ID);
        when(grandPrixRepository.findByIdAndFetchRaceResults(GP_ID)).thenReturn(Optional.of(grandPrix));
        //when
        GrandPrix grandPrixFromService = grandPrixService.getByIdWithRaceResults(GP_ID);
        //then
        assertEquals(GP_ID, grandPrixFromService.getId());
    }

    @Test
    void shouldThrowExceptionIfEntityNotFoundForGetByIdWithQualificationResults() {
        //given
        when(grandPrixRepository.findByIdAndFetchQualificationResults(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(EntityNotFoundException.class, () -> grandPrixService.getByIdWithQualificationResults(2L),
                "GrandPrix was not found for parameter 2");
    }

    @Test
    void shouldReturnGrandPrixForGetByIdWithQualificationResults() {
        //given
        final long GP_ID = 3L;
        GrandPrix grandPrix = new GrandPrix();
        grandPrix.setId(GP_ID);
        when(grandPrixRepository.findByIdAndFetchQualificationResults(GP_ID)).thenReturn(Optional.of(grandPrix));
        //when
        GrandPrix grandPrixFromService = grandPrixService.getByIdWithQualificationResults(GP_ID);
        //then
        assertEquals(GP_ID, grandPrixFromService.getId());
    }

    @Test
    void shouldThrowExceptionIfListEmptyForGetAllByYear() {
        //given
        when(grandPrixRepository.findAllByYear(anyInt())).thenReturn(new ArrayList<>());
        //then
        assertThrows(EntityNotFoundException.class, () -> grandPrixService.getAllByYear(2010),
                "GrandPrix was not found for parameter YEAR = 2010");
    }

    @Test
    void shouldReturnGrandPrixListForGetAllByYear() {
        //given
        List<GrandPrix> grandPrixes = new ArrayList<>();
        grandPrixes.add(new GrandPrix());
        grandPrixes.add(new GrandPrix());
        when(grandPrixRepository.findAllByYear(anyInt())).thenReturn(grandPrixes);
        //when
        List<GrandPrix> grandPrixesFromService = grandPrixService.getAllByYear(2010);
        //then
        assertEquals(2, grandPrixesFromService.size());
    }

    @Test
    void shouldThrowExceptionIfListEmptyForGetByCircuitId() {
        //given
        when(grandPrixRepository.findByCircuitId(anyLong())).thenReturn(new ArrayList<>());
        //then
        assertThrows(EntityNotFoundException.class, () -> grandPrixService.getByCircuitId(5L),
                "GrandPrix was not found for parameter CIRCUIT ID = 5L");
    }

    @Test
    void shouldReturnGrandPrixListForGetByCircuitId() {
        //given
        List<GrandPrix> grandPrixes = new ArrayList<>();
        grandPrixes.add(new GrandPrix());
        grandPrixes.add(new GrandPrix());
        when(grandPrixRepository.findByCircuitId(anyLong())).thenReturn(grandPrixes);
        //when
        List<GrandPrix> grandPrixesFromService = grandPrixService.getByCircuitId(5L);
        //then
        assertEquals(2, grandPrixesFromService.size());
    }
}