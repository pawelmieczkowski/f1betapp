package pl.salata.f1betapp.login;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.salata.f1betapp.login.exception.TokenRefreshException;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class LoginController {

    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentials credentials) {
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getAppUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok()
                            .header("Authorization", "Bearer " + token)
                            .body(requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

}
