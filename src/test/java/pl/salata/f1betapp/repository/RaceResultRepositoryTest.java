package pl.salata.f1betapp.repository;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.test.context.jdbc.Sql;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.model.RaceResult;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql("/scripts/grandPrixRepositoryTestData.sql")
@DataJpaTest
@PropertySources(@PropertySource("classpath:application-statistics.properties"))
class RaceResultRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RaceResultRepository raceResultRepository;

    @BeforeEach
    void setUp() {
        Session session = testEntityManager.getEntityManager().unwrap(Session.class);
        session.getSessionFactory().getStatistics().clear();
    }

    @Test
    void shouldReturnRaceResultWithGrandPrixByDriverIdUsingSingleQuery() {
        //when
        List<RaceResult> raceResultsFromRepository = raceResultRepository.findByDriver(1L);
        //making sure grand prix is fetched
        System.out.println(raceResultsFromRepository.get(0).getGrandPrix());

        Session session = testEntityManager.getEntityManager().unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        long queryCount = statistics.getPrepareStatementCount();

        //then
        assertThat(raceResultsFromRepository).extracting(RaceResult::getDriverId).containsOnly(1L);
        assertEquals(2, raceResultsFromRepository.size());
        assertEquals(1, queryCount);
    }

    @Test
    void shouldReturnRaceResultWithGrandPrixByTeamNameAndByYearUsingSingleQuery() {
        //when
        List<RaceResult> raceResultsFromRepository = raceResultRepository.findByTeam("BMW Sauber", 2009);
        //making sure grand prix is fetched
        System.out.println(raceResultsFromRepository.get(0).getGrandPrix());
        System.out.println(raceResultsFromRepository.get(1).getGrandPrix());

        Session session = testEntityManager.getEntityManager().unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        long queryCount = statistics.getPrepareStatementCount();

        //then
        assertThat(raceResultsFromRepository).extracting(RaceResult::getTeamName).containsOnly("BMW Sauber");
        assertThat(raceResultsFromRepository).extracting(RaceResult::getGrandPrix).extracting(GrandPrix::getYear).containsOnly(2009);
        assertEquals(2, raceResultsFromRepository.size());
        assertEquals(1, queryCount);
    }

    @Test
    void shouldReturnDistinctYearsByTeamName() {
        //when
        List<Long> yearFromRepository = raceResultRepository.findAllYearsByTeam("BMW Sauber");
        //then
        assertEquals(2, yearFromRepository.size());
        assertArrayEquals(new Long[]{2009L, 2010L}, yearFromRepository.toArray());
        assertEquals(new HashSet<>(yearFromRepository).size(), yearFromRepository.size());
    }
}