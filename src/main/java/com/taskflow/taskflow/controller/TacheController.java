package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.model.Tache;
import com.taskflow.taskflow.service.TacheService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/taches")

public class TacheController {

    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }


    @PostMapping
    public Tache createTache(
            @Valid @RequestBody Tache tache) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return tacheService.saveTacheParUtilisateur(tache, email);
    }

    @DeleteMapping("/{id}")
    public void deleteTache(@PathVariable Integer id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        tacheService.deleteTacheByIdAndEmail(email, id);
    }


    @GetMapping
    public List<Tache> getMesTaches() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return tacheService.getTachesParEmail(email);
    }

    @PutMapping("/{id}/statut/{idStatut}")
    public Tache modifierStatut(
            @PathVariable Integer id,
            @PathVariable Integer idStatut) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return tacheService.modifierStatut(id, idStatut, email);
    }

}