package com.quizplatform.security;

import com.quizplatform.config.JwtProperties;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private JwtProperties properties;

    @BeforeEach
    void setup() {
        properties = new JwtProperties();
        // 32-byte secret minimum recommended
        properties.setSecret("Vbl4PQqpluriYmxD5qb5/c9BfrGqLDD+SrTmo38QvO4=");
        properties.setExpirationMs(3600000); // 1 hour
        jwtUtil = new JwtUtil(properties);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String token = jwtUtil.generateToken("test@example.com");
        
        assertThat(token).isNotNull().isNotEmpty();
        assertThat(token).contains(".");
    }

    @Test
    void extractSubject_shouldReturnCorrectSubject() {
        String subject = "user@test.com";
        String token = jwtUtil.generateToken(subject);

        String extracted = jwtUtil.extractSubject(token);

        assertThat(extracted).isEqualTo(subject);
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken("test@example.com");

        boolean isValid = jwtUtil.validateToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.string";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertThat(isValid).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalseForMalformedToken() {
        String malformed = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.malformed";

        boolean isValid = jwtUtil.validateToken(malformed);

        assertThat(isValid).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalseForEmptyToken() {
        boolean isValid = jwtUtil.validateToken("");

        assertThat(isValid).isFalse();
    }

    @Test
    void extractSubject_shouldThrowForInvalidToken() {
        String invalidToken = "invalid.token.here";

        assertThatThrownBy(() -> jwtUtil.extractSubject(invalidToken))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void generateToken_multipleTokens_shouldNotBeNull() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user1");

        // at minimum both tokens should be produced and contain the subject
        assertThat(token1).isNotEmpty();
        assertThat(token2).isNotEmpty();
        assertThat(jwtUtil.extractSubject(token1)).isEqualTo("user1");
        assertThat(jwtUtil.extractSubject(token2)).isEqualTo("user1");
    }
}
