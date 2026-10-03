package com.taskflow.taskflow.security;
import com.taskflow.taskflow.model.Utilisateur;
import com.taskflow.taskflow.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.taskflow.taskflow.security.JwtService;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthController(JwtService jwtService,
                          UtilisateurRepository utilisateurRepository,
                          PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
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


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("Email déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        utilisateur.setRole("USER");
        utilisateur.setDateCreation(java.time.LocalDateTime.now());

        utilisateurRepository.save(utilisateur);

        String token = jwtService.generateToken(utilisateur.getEmail());
        return ResponseEntity.ok(token);
    }

}