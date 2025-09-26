package com.example.owasp.a07;

import com.example.owasp.dto.Message;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Base64;

@RestController
@RequestMapping("/a07")
public class A07AuthFailuresController {
    private final SecretKey key = Keys.hmacShaKeyFor(System.getenv().getOrDefault("JWT_SECRET","change-this-demo-secret-change-this-demo-secret").getBytes());

    // INSECURE: decodes JWT without signature verification (DO NOT DO THIS)
    @PostMapping("/insecure/parse")
    public Message insecureParse(@RequestParam String jwt) {
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) return new Message("Invalid JWT");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        return new Message("payload=" + payload);
    }

    // SECURE: verifies signature
    @PostMapping("/secure/parse")
    public Message secureParse(@RequestParam String jwt) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return new Message("sub=" + jws.getBody().getSubject());
        } catch (JwtException e) {
            return new Message("Invalid signature");
        }
    }
}
