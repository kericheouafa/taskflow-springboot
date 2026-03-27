package com.taskflow.taskflow.service;

import com.taskflow.taskflow.model.Tache;
import com.taskflow.taskflow.repository.TacheRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TacheService {

    private final TacheRepository tacheRepository;

    public TacheService(TacheRepository tacheRepository) {
        this.tacheRepository = tacheRepository;
    }

    public List<Tache> getAllTaches() {
        return tacheRepository.findAll();
    }

    public Tache saveTache(Tache tache) {
        return tacheRepository.save(tache);
    }

    public void deleteTache(int id) {
        tacheRepository.deleteById(id);
    }
}