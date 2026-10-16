package com.taskflow.taskflow.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                "cle-secrete-de-test-assez-longue-pour-hs256-32-octets");
        ReflectionTestUtils.setField(jwtService, "expiration", 60000L);
    }

    @Test
    void generateToken_doitRetournerUnTokenValide() {
        String token = jwtService.generateToken("wafa@test.com");

        assertNotNull(token);
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void extractEmail_doitRetrouverLEmailDuToken() {
        String token = jwtService.generateToken("wafa@test.com");
        String email = jwtService.extractEmail(token);
        assertEquals("wafa@test.com", email);
    }
@Test
    void validateToken_doitRefuserUnTokenFalsifie() {
        String tokenBidon = "ceci.nest.pas.un.token";
        boolean resultat = jwtService.validateToken(tokenBidon);
        assertFalse(resultat);
}

    @Test
    void validateToken_doitRefuserUnTokenSigneAvecUneAutreCle() {
        JwtService autreService = new JwtService();
        ReflectionTestUtils.setField(autreService, "secret",
                "une-autre-cle-completement-differente-32-octets-minimum");
        ReflectionTestUtils.setField(autreService, "expiration", 60000L);

        String tokenPirate = autreService.generateToken("wafa@test.com");

        assertFalse(jwtService.validateToken(tokenPirate));
    }

    @Test
    void validateToken_doitRefuserUnTokenExpire() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1000L);

        String tokenExpire = jwtService.generateToken("wafa@test.com");

        assertFalse(jwtService.validateToken(tokenExpire));
    }

    @Test
    void extractEmail_doitLeverUneExceptionPourUnTokenInvalide() {
        assertThrows(Exception.class, () -> jwtService.extractEmail("ceci.nest.pas.un.token"));

    }

    @Test
    void extractEmail_doitLeverUneExceptionPourUnTokenExpire() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1000L);

        String tokenExpire = jwtService.generateToken("wafa@test.com");

        assertThrows(ExpiredJwtException.class, () -> jwtService.extractEmail(tokenExpire));
    }

}