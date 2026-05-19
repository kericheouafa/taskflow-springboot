package com.taskflow.taskflow.security;

import com.taskflow.taskflow.model.Utilisateur;
import com.taskflow.taskflow.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(request.getEmail());

        if (utilisateur.isEmpty()) {
            return ResponseEntity.status(401).body("Email incorrect");
        }

        if (!passwordEncoder.matches(request.getPassword(), utilisateur.get().getMotDePasse())) {
            return ResponseEntity.status(401).body("Mot de passe incorrect");
        }

        String token = jwtService.generateToken(utilisateur.get().getEmail());
        return ResponseEntity.ok(token);
    }
}