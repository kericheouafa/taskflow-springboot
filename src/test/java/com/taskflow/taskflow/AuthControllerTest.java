package com.taskflow.taskflow.security;

import com.taskflow.taskflow.model.Utilisateur;
import com.taskflow.taskflow.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UtilisateurRepository utilisateurRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    // ---------- login ----------

    @Test
    void login_doitRetourner200EtLeTokenSiIdentifiantsCorrects() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("wafa@test.com");
        utilisateur.setMotDePasse("hash-du-mot-de-passe");

        when(utilisateurRepository.findByEmail("wafa@test.com"))
                .thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("secret123", "hash-du-mot-de-passe"))
                .thenReturn(true);
        when(jwtService.generateToken("wafa@test.com"))
                .thenReturn("faux-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wafa@test.com\",\"password\":\"secret123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("faux-token"));
    }

    @Test
    void login_doitRetourner401SiMauvaisMotDePasse() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("wafa@test.com");
        utilisateur.setMotDePasse("hash-du-mot-de-passe");

        when(utilisateurRepository.findByEmail("wafa@test.com"))
                .thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("mauvais123", "hash-du-mot-de-passe"))
                .thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wafa@test.com\",\"password\":\"mauvais123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email ou mot de passe incorrect"));

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_doitRetourner401SiEmailInconnu() throws Exception {
        when(utilisateurRepository.findByEmail("inconnu@test.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"inconnu@test.com\",\"password\":\"secret123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email ou mot de passe incorrect"));

        verify(jwtService, never()).generateToken(any());
    }

    // ---------- register ----------

    @Test
    void register_doitRetourner200EtLeTokenSiEmailLibre() throws Exception {
        when(utilisateurRepository.findByEmail("nouveau@test.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("hash");
        when(jwtService.generateToken("nouveau@test.com")).thenReturn("faux-token");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"nouveau@test.com\",\"password\":\"secret123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("faux-token"));

        verify(utilisateurRepository).save(any(Utilisateur.class));
    }

    @Test
    void register_doitRetourner409SiEmailDejaUtilise() throws Exception {
        when(utilisateurRepository.findByEmail("wafa@test.com"))
                .thenReturn(Optional.of(new Utilisateur()));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wafa@test.com\",\"password\":\"secret123\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.email").value("Email déjà utilisé"));

        verify(utilisateurRepository, never()).save(any());
    }

    // ---------- refresh ----------

    @Test
    void refresh_doitRetournerUnNouveauToken() throws Exception {
        when(jwtService.generateToken("wafa@test.com")).thenReturn("nouveau-token");

        mockMvc.perform(post("/api/auth/refresh")
                        .principal(new UsernamePasswordAuthenticationToken("wafa@test.com", null)))
                .andExpect(status().isOk())
                .andExpect(content().string("nouveau-token"));
    }
}