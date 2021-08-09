package pl.salata.f1betapp.login;

import lombok.Getter;
import lombok.Setter;
import pl.salata.f1betapp.login.appuser.AppUserRole;

import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String username;
    private String email;
    private List<String> appUserRole;
}
