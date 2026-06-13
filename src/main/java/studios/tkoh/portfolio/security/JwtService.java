package studios.tkoh.portfolio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author Studios TKOH!
 */
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.issuer:portfolio-hub}")
    private String issuer;

    @Value("${application.security.jwt.audience:portfolio-hub-api}")
    private String audience;

    private SecretKey signingKey;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        byte[] keyBytes = decodeSecretKey(secretKey);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret must be at least 256 bits");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parser()
                .verifyWith(this.signingKey)
                .requireIssuer(issuer)
                .requireAudience(audience)
                .build();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {

        if (!(userDetails instanceof CustomUserDetails customUserDetails)) {
            throw new IllegalArgumentException("UserDetails must be an instance of CustomUserDetails");
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("profileId", customUserDetails.getProfileId());
        extraClaims.put("userId", customUserDetails.getUserId());
        extraClaims.put("roles", userDetails.getAuthorities().stream().map(Object::toString).toList());

        return this.generateToken(extraClaims, userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        long expirationTimeMillis = jwtExpiration * 60 * 1000;
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTimeMillis);

        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(userDetails.getUsername())
                .issuer(issuer)
                .audience().add(audience).and()
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(expirationDate)
                .and()
                .signWith(this.signingKey)
                .compact();
    }

    public int getAccessTokenMaxAgeSeconds() {
        return Math.toIntExact(jwtExpiration * 60);
    }

    private byte[] decodeSecretKey(String configuredSecret) {
        try {
            return Decoders.BASE64.decode(configuredSecret);
        } catch (IllegalArgumentException ex) {
            return configuredSecret.getBytes(StandardCharsets.UTF_8);
        }
    }

    private Claims extractAllClaims(String token) throws io.jsonwebtoken.JwtException {
        return this.jwtParser
                .parseSignedClaims(token)
                .getPayload();
    }
}
