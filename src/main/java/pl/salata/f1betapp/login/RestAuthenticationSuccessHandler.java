package pl.salata.f1betapp.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.salata.f1betapp.login.appuser.AppUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RefreshTokenService refreshTokenService;
    private final long expirationTime;
    private final String secret;
    private final ObjectMapper mapper;

    public RestAuthenticationSuccessHandler(
            @Value("${jwt.expirationTime}") long expirationTime,
            @Value("${jwt.secret}") String secret,
            MappingJackson2HttpMessageConverter messageConverter,
            RefreshTokenService refreshTokenService) {
        this.expirationTime = expirationTime;
        this.secret = secret;
        this.mapper = messageConverter.getObjectMapper();
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        AppUser principal = (AppUser) authentication.getPrincipal();
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
        response.addHeader("Authorization", "Bearer " + token);

        String refreshToken = refreshTokenService.createRefreshToken(principal.getId()).getToken();
        response.addHeader("Refresh", "Bearer " + refreshToken);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(principal.getUsername());
        loginResponse.setEmail(principal.getEmail());
        loginResponse.setAppUserRole(principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        mapper.writeValue(writer, loginResponse);
        writer.flush();
    }
}
