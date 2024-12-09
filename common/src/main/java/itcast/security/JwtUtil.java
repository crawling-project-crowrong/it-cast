package itcast.security;


import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
	@Value("${jwt.secret.key}")
	private String secretKey;
	private final long expirationTime = 1000L * 60 * 60;
	public static final String AUTHORIZATION_HEADER = "Authorization";

	public String createToken(String email) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationTime);

		return Jwts.builder()
			.setSubject(email)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, secretKey)
			.compact();
	}

	public String extractEmail(String token) {
		Claims claims = Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token)
			.getBody();
		return claims.getSubject();
	}

	public boolean isTokenExpired(String token) {
		Date expiration = Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token)
			.getBody()
			.getExpiration();
		return expiration.before(new Date());
	}

	public boolean validateToken(String token, String email) {
		String extractedEmail = extractEmail(token);
		return (extractedEmail.equals(email) && !isTokenExpired(token));
	}
}