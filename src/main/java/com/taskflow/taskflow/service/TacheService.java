package com.taskflow.taskflow.service;

import com.taskflow.taskflow.model.Statut;
import com.taskflow.taskflow.model.Tache;
import com.taskflow.taskflow.model.Utilisateur;
import com.taskflow.taskflow.repository.StatutRepository;
import com.taskflow.taskflow.repository.TacheRepository;
import com.taskflow.taskflow.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TacheService {

    private final TacheRepository tacheRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final StatutRepository statutRepository;

    public TacheService(
            TacheRepository tacheRepository,
            UtilisateurRepository utilisateurRepository,
            StatutRepository statutRepository) {

        this.tacheRepository = tacheRepository;
        this.utilisateurRepository = utilisateurRepository;
       this.statutRepository = statutRepository;
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


    public List<Tache> getTachesParEmail(String email)
    {
        return tacheRepository.findByUtilisateurEmail(email);
    }


    public Tache saveTacheParUtilisateur(Tache tache, String email) {

        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        tache.setUtilisateur(utilisateur);
        tache.setId(0);
        Statut statutAFaire = statutRepository.findByLibelle("A faire")
                .orElseThrow(() -> new RuntimeException("Statut À faire introuvable"));

        tache.setStatut(statutAFaire);

        return tacheRepository.save(tache);
    }


    public Tache modifierStatut(Integer idTache, Integer idStatut, String email) {

        Tache tache = tacheRepository
                .findByUtilisateurEmailAndId(email, idTache)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable"));

        Statut statut = statutRepository
                .findById(idStatut)
                .orElseThrow(() -> new RuntimeException("Statut introuvable"));

        tache.setStatut(statut);

        return tacheRepository.save(tache);
    }




}