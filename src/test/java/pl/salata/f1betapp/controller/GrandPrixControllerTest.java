package pl.salata.f1betapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.GrandPrixService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GrandPrixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GrandPrixService grandPrixService;

    @Test
    void shouldReturnCollectionOfGrandPrixWithoutResults() throws Exception {
        //given
        List<GrandPrix> results = generateCollectionOfGrandPrix();
        when(grandPrixService.getAllByYear(anyInt())).thenReturn(results);
        //then
        mockMvc.perform(get("/grands-prix")
                .param("year", "2001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].year", is(2001)))
                .andExpect(jsonPath("$[0].round", is(3)))
                .andExpect(jsonPath("$[0].name", is("name1")))
                .andExpect(jsonPath("$[0].date", is("2001-01-21")))
                .andExpect(jsonPath("$[0].localization", is("localization1")))
                .andExpect(jsonPath("$[0].country", is("country1")))
                .andExpect(jsonPath("$[0].time", is("01:02:03")))
                .andExpect(jsonPath("$[0].url", is("url1")))
                .andExpect(jsonPath("$[0].driverName", is("driverName1")))
                .andExpect(jsonPath("$[0].circuit.id", is(51)))
                .andExpect(jsonPath("$[0].raceResult").doesNotExist())
                .andExpect(jsonPath("$[0].qualificationResult").doesNotExist())

                .andExpect(jsonPath("$[1].circuit.id", is(52)))
                .andExpect(jsonPath("$[1].raceResult").doesNotExist())
                .andExpect(jsonPath("$[1].qualificationResult").doesNotExist());
    }

    @Test
    void shouldReturnGrandPrixWithoutResults() throws Exception {
        //given
        GrandPrix results = generateGreatPrix();
        when(grandPrixService.getById(anyLong())).thenReturn(results);
        //then
        mockMvc.perform(get("/grands-prix/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.year", is(2001)))
                .andExpect(jsonPath("$.round", is(3)))
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.date", is("2001-01-21")))
                .andExpect(jsonPath("$.localization", is("localization1")))
                .andExpect(jsonPath("$.country", is("country1")))
                .andExpect(jsonPath("$.time", is("01:02:03")))
                .andExpect(jsonPath("$.url", is("url1")))
                .andExpect(jsonPath("$.driverName", is("driverName1")))
                .andExpect(jsonPath("$.circuit.id", is(51)))
                .andExpect(jsonPath("$.raceResult").doesNotExist())
                .andExpect(jsonPath("$.qualificationResult").doesNotExist());
    }

    @Test
    void shouldReturnGrandPrixWithoutQualificationResults() throws Exception {
        //given
        GrandPrix results = generateGreatPrix();
        when(grandPrixService.getByIdWithRaceResults(anyLong())).thenReturn(results);
        //then
        mockMvc.perform(get("/grands-prix/{id}/results", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.year", is(2001)))
                .andExpect(jsonPath("$.round", is(3)))
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.date", is("2001-01-21")))
                .andExpect(jsonPath("$.localization", is("localization1")))
                .andExpect(jsonPath("$.country", is("country1")))
                .andExpect(jsonPath("$.time", is("01:02:03")))
                .andExpect(jsonPath("$.url", is("url1")))
                .andExpect(jsonPath("$.driverName", is("driverName1")))
                .andExpect(jsonPath("$.raceResult", hasSize(1)))
                .andExpect(jsonPath("$.circuit.id", is(51)))
                .andExpect(jsonPath("$.qualificationResult").doesNotExist());
    }

    @Test
    void shouldReturnGrandPrixWithoutRaceResults() throws Exception {
        //given
        GrandPrix results = generateGreatPrix();
        when(grandPrixService.getByIdWithQualificationResults(anyLong())).thenReturn(results);
        //then
        mockMvc.perform(get("/grands-prix/{id}/qualifications", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.year", is(2001)))
                .andExpect(jsonPath("$.round", is(3)))
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.date", is("2001-01-21")))
                .andExpect(jsonPath("$.localization", is("localization1")))
                .andExpect(jsonPath("$.country", is("country1")))
                .andExpect(jsonPath("$.time", is("01:02:03")))
                .andExpect(jsonPath("$.url", is("url1")))
                .andExpect(jsonPath("$.driverName", is("driverName1")))
                .andExpect(jsonPath("$.circuit.id", is(51)))
                .andExpect(jsonPath("$.qualificationResult", hasSize(1)))
                .andExpect(jsonPath("$.raceResult").doesNotExist());

    }

    @Test
    void shouldReturnCollectionOfGrandPrixWithoutResultsAndCircuits() throws Exception {
        //given
        List<GrandPrix> results = generateCollectionOfGrandPrix();
        when(grandPrixService.getByCircuitId(anyLong())).thenReturn(results);
        //then
        mockMvc.perform(get("/grands-prix/circuit")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].year", is(2001)))
                .andExpect(jsonPath("$[0].round", is(3)))
                .andExpect(jsonPath("$[0].name", is("name1")))
                .andExpect(jsonPath("$[0].date", is("2001-01-21")))
                .andExpect(jsonPath("$[0].localization", is("localization1")))
                .andExpect(jsonPath("$[0].country", is("country1")))
                .andExpect(jsonPath("$[0].time", is("01:02:03")))
                .andExpect(jsonPath("$[0].url", is("url1")))
                .andExpect(jsonPath("$[0].driverName", is("driverName1")))
                .andExpect(jsonPath("$[0].circuit.id").doesNotExist())
                .andExpect(jsonPath("$[0].raceResult").doesNotExist())
                .andExpect(jsonPath("$[0].qualificationResult").doesNotExist())

                .andExpect(jsonPath("$[1].circuit.id").doesNotExist())
                .andExpect(jsonPath("$[1].raceResult").doesNotExist())
                .andExpect(jsonPath("$[1].qualificationResult").doesNotExist());
    }

    private List<GrandPrix> generateCollectionOfGrandPrix() {
        List<GrandPrix> data = new ArrayList<>();
        GrandPrix grandPrix1 = generateGreatPrix();
        data.add(grandPrix1);

        GrandPrix grandPrix2 = new GrandPrix();
        grandPrix2.setId(2L);
        Circuit circuit2 = new Circuit();
        circuit2.setId(52L);
        grandPrix2.setCircuit(circuit2);
        grandPrix2.setRaceResult(Collections.singletonList(new RaceResult()));
        grandPrix2.setQualificationResult(Collections.singletonList(new QualificationResult()));
        data.add(grandPrix2);

        return data;
    }

    private GrandPrix generateGreatPrix() {
        GrandPrix grandPrix = new GrandPrix();
        grandPrix.setId(1L);
        grandPrix.setYear(2001);
        grandPrix.setRound(3);
        Circuit circuit = new Circuit();
        circuit.setId(51L);
        grandPrix.setCircuit(circuit);
        grandPrix.setName("name1");
        grandPrix.setDate(LocalDate.of(2001, 1, 21));
        grandPrix.setLocalization("localization1");
        grandPrix.setCountry("country1");
        grandPrix.setTime(LocalTime.of(1, 2, 3));
        grandPrix.setUrl("url1");
        grandPrix.setDriverName("driverName1");
        grandPrix.setRaceResult(Collections.singletonList(new RaceResult()));
        grandPrix.setQualificationResult(Collections.singletonList(new QualificationResult()));
        return grandPrix;
    }
}