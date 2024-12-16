package itcast.auth.jwt;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Getter
@Component
public class JwtProvider {

    private final SecretKey secretKey;

    public JwtProvider() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}
