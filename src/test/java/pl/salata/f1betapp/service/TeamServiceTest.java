package pl.salata.f1betapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.repository.TeamRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    void shouldThrowExceptionIfEntityNotFoundForGetById() {
        //given
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> teamService.getById(1L));
        assertEquals("Team was not found for parameter 1", exception.getMessage());
    }

    @Test
    void shouldReturnTeamForGetById() {
        //given
        final long GP_ID = 3L;
        Team team = new Team();
        team.setId(GP_ID);
        when(teamRepository.findById(GP_ID)).thenReturn(Optional.of(team));
        //when
        Team teamFromService = teamService.getById(GP_ID);
        //then
        assertEquals(GP_ID, teamFromService.getId());
    }

    @Test
    void shouldThrowExceptionIfEntityNotFoundForGetByName() {
        //given
        when(teamRepository.findByName(anyString())).thenReturn(Optional.empty());
        //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> teamService.getByName("teamName"));
        assertEquals("Team was not found for parameter teamName", exception.getMessage());
    }

    @Test
    void shouldReturnTeamForGetByName() {
        //given
        final String TEAM_NAME = "teamName5";
        Team team = new Team();
        team.setName(TEAM_NAME);
        when(teamRepository.findByName(TEAM_NAME)).thenReturn(Optional.of(team));
        //when
        Team teamFromService = teamService.getByName(TEAM_NAME);
        //then
        assertEquals(TEAM_NAME, teamFromService.getName());
    }
}