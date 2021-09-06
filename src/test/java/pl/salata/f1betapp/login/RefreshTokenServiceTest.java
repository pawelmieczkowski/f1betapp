package pl.salata.f1betapp.login;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.salata.f1betapp.login.exception.TokenRefreshException;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    Clock FIXED = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.systemDefault());

    @Mock
    Clock clock;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    RefreshTokenService refreshTokenService;

    @Test
    void shouldReturnToken() {
        //given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setToken("tokenValue1");
        refreshToken.setExpiryDate(Instant.parse("2007-12-03T10:15:31.00Z"));

        when(clock.instant()).thenReturn(FIXED.instant());
        //when
        RefreshToken tokenFromService = refreshTokenService.verifyExpiration(refreshToken, clock);
        //then
        assertEquals(1, tokenFromService.getId());
        assertEquals("tokenValue1", tokenFromService.getToken());
    }

    @Test
    void shouldThrowTokenRefreshException() {
        //given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(2L);
        refreshToken.setToken("tokenValue2");
        refreshToken.setExpiryDate(Instant.parse("2007-12-03T10:15:29.00Z"));

        doNothing().when(refreshTokenRepository).delete(any(RefreshToken.class));
        when(clock.instant()).thenReturn(FIXED.instant());
        //then
        TokenRefreshException exceptionThrown = assertThrows(TokenRefreshException.class, () -> refreshTokenService.verifyExpiration(refreshToken, clock));
        assertEquals("Failed for [tokenValue2]: Refresh token was expired. Please make a new signin request",exceptionThrown.getMessage());

    }
}