package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.service.TeamService;

@AllArgsConstructor
@RequestMapping("teams")
@RestController
@CrossOrigin
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public Team getByName(@RequestParam String name) {
        return teamService.getByName(name);
    }

    @GetMapping("{id}")
    public Team getTeam(@PathVariable Long id) {
        return teamService.findById(id);
    }
}
