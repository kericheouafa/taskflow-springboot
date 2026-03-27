package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.model.Tache;
import com.taskflow.taskflow.service.TacheService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/taches")
public class TacheController {

    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping
    public List<Tache> getAllTaches() {
        return tacheService.getAllTaches();
    }

    @PostMapping
    public Tache createTache(@RequestBody Tache tache) {
        return tacheService.saveTache(tache);
    }

    @DeleteMapping("/{id}")
    public void deleteTache(@PathVariable int id) {
        tacheService.deleteTache(id);
    }
}