package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.repository.TeamRepository;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team getById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Team.class, String.valueOf(id)));
    }

    public Team getByName(String name) {
        return teamRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(Team.class, String.valueOf(name)));
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}
