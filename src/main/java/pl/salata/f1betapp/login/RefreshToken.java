package pl.salata.f1betapp.login;

import lombok.Getter;
import lombok.Setter;
import pl.salata.f1betapp.login.appuser.AppUser;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
