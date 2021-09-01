package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.service.TeamService;

import java.util.List;

@AllArgsConstructor
@RequestMapping("teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public Team getByName(@RequestParam String name) {
        return teamService.getByName(name);
    }

    @GetMapping("{id}")
    public Team getTeam(@PathVariable Long id) {
        return teamService.getById(id);
    }

    @GetMapping("all")
    public List<Team> getAllTeams(){
        return teamService.getAllTeams();
    }
}
