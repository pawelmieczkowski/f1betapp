package pl.salata.f1betapp.repository;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.RaceResult;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Sql("/scripts/grandPrixRepository.sql")
@DataJpaTest
class GrandPrixRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private GrandPrixRepository grandPrixRepository;

    @BeforeEach
    void setUp() {
        Session session = testEntityManager.getEntityManager().unwrap(Session.class);
        session.getSessionFactory().getStatistics().clear();
    }

    @Test
    void shouldReturnAllGrandPrixWithCircuitByAYearUsingSingleQuery() {
        //when
        List<GrandPrix> grandPrixesFromRepository = grandPrixRepository.findAllByYear(2009);
        //making sure, the circuit is fetched
        System.out.println(grandPrixesFromRepository.get(0).getCircuit().getName());

        Session session = testEntityManager.getEntityManager().unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        long queryCount = statistics.getPrepareStatementCount();

        //then
        assertEquals(2, grandPrixesFromRepository.size());
        assertEquals(1, queryCount);
    }

    @Test
    void shouldReturnGrandPrixWithRaceResultsAndCircuitByIdUsingSingleQuery() {
        //when
        Optional<GrandPrix> grandPrixFromRepository = grandPrixRepository.findByIdAndFetchRaceResults(1L);
        GrandPrix grandPrixValue = grandPrixFromRepository.orElseThrow();
        List<RaceResult> raceResults = grandPrixValue.getRaceResult();
        Circuit circuit = grandPrixValue.getCircuit();

        //making sure the results and circuit are fetched
        System.out.println(raceResults.size());
        System.out.println(circuit.getName());

        Session session = testEntityManager.getEntityManager().unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        long queryCount = statistics.getPrepareStatementCount();
        //then
        assertEquals(2, raceResults.size());
        assertEquals(1, circuit.getId());
        assertEquals(1, queryCount);
    }

    @Test
    void shouldReturnGrandPrixWithQualificationResultsAndCircuitByIdUsingSingleQuery() {
        //when
        Optional<GrandPrix> grandPrixFromRepository = grandPrixRepository.findByIdAndFetchQualificationResults(1L);
        GrandPrix grandPrixValue = grandPrixFromRepository.orElseThrow();
        List<QualificationResult> qualificationResults = grandPrixValue.getQualificationResult();
        Circuit circuit = grandPrixValue.getCircuit();
        //making sure the results and circuit are fetched
        System.out.println(qualificationResults.size());
        System.out.println(circuit.getName());

        Session session = testEntityManager.getEntityManager().unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        long queryCount = statistics.getPrepareStatementCount();
        //then
        assertEquals(2, qualificationResults.size());
        assertEquals(1, circuit.getId());
        assertEquals(1, queryCount);
    }

    @Test
    void shouldReturnDistinctYears() {
        //when
        List<Long> yearsFromRepository = grandPrixRepository.findAllYears();
        //then
        assertEquals(2, yearsFromRepository.size());
        assertEquals(new HashSet<>(yearsFromRepository).size(), yearsFromRepository.size());
    }
}