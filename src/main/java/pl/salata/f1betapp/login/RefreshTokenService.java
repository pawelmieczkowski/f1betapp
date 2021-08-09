package pl.salata.f1betapp.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.salata.f1betapp.login.appuser.AppUserRepository;
import pl.salata.f1betapp.login.exception.TokenRefreshException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final Long refreshExpirationTime;

    private final RefreshTokenRepository refreshTokenRepository;
    private final AppUserRepository appUserRepository;

    public RefreshTokenService(@Value("${jwt.refreshExpirationTime}") Long refreshExpirationTime, RefreshTokenRepository refreshTokenRepository, AppUserRepository appUserRepository) {
        this.refreshExpirationTime = refreshExpirationTime;
        this.refreshTokenRepository = refreshTokenRepository;
        this.appUserRepository = appUserRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setAppUser(appUserRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("wrong user Id")));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationTime));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByAppUserId(userId);
    }
}
