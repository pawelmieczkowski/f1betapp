package pl.salata.f1betapp.service;

import org.springframework.stereotype.Service;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.repository.TeamRepository;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team findById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id provided"));
    }

    public Team getByName(String name) {
        return teamRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("No driver with provided name"));
    }
}
