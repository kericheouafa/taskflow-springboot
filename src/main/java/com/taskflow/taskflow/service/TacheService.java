package com.taskflow.taskflow.service;

import com.taskflow.taskflow.model.Tache;
import com.taskflow.taskflow.model.Utilisateur;
import com.taskflow.taskflow.repository.TacheRepository;
import com.taskflow.taskflow.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TacheService {

    private final TacheRepository tacheRepository;
    private final UtilisateurRepository utilisateurRepository;

    public TacheService(
            TacheRepository tacheRepository,
            UtilisateurRepository utilisateurRepository) {

        this.tacheRepository = tacheRepository;
        this.utilisateurRepository = utilisateurRepository;
    }



    public Tache saveTache(Tache tache) {
        return tacheRepository.save(tache);
    }

    public void deleteTache(int id) {
        tacheRepository.deleteById(id);
    }

    public void deleteTacheByIdAndEmail(String email, Integer id) {

        Tache tache = tacheRepository
                .findByUtilisateurEmailAndId(email, id)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable"));

        tacheRepository.delete(tache);
    }


    public List<Tache> getTachesParEmail(String email) {
        return tacheRepository.findByUtilisateurEmail(email);
    }
    public Tache saveTacheParUtilisateur(Tache tache, String email) {

        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        tache.setUtilisateur(utilisateur);
        tache.setId(0);

        return tacheRepository.save(tache);
    }


}