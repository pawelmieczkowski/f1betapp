package pl.salata.f1betapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.service.TeamService;

@AllArgsConstructor
@RequestMapping("teams")
@RestController
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public Team getByName(@RequestParam String name) {
        return teamService.getByName(name);
    }
}
