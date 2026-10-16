package com.taskflow.taskflow;

import com.taskflow.taskflow.model.Statut;
import com.taskflow.taskflow.model.Tache;
import com.taskflow.taskflow.model.Utilisateur;
import com.taskflow.taskflow.repository.StatutRepository;
import com.taskflow.taskflow.repository.TacheRepository;
import com.taskflow.taskflow.repository.UtilisateurRepository;
import com.taskflow.taskflow.service.TacheService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TacheServiceTest {

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private StatutRepository statutRepository;

    @InjectMocks
    private TacheService tacheService;

    @Test
    void getTachesParEmail_doitRetournerLesTachesDeLUtilisateur() {
        Tache t1 = new Tache();
        t1.setTitre("Tache 1");
        Tache t2 = new Tache();
        t2.setTitre("Tache 2");
        when(tacheRepository.findByUtilisateurEmail("wafa@test.com")) //quand on veut findByUtilisateurEmail on donne la liste de ces 2 tâche
                .thenReturn(List.of(t1, t2));

        List<Tache> resultat = tacheService.getTachesParEmail("wafa@test.com");

        assertEquals(List.of(t1, t2), resultat);

        verify(tacheRepository).findByUtilisateurEmail("wafa@test.com"); // vérifie que le service a bien appemé findByUtilisateurEmail
    }

    @Test
    void saveTache_doitEnregistrerEtRetournerLaTache() {
        Tache tache = new Tache();
        when(tacheRepository.save(tache)).thenReturn(tache);
        Tache resultat = tacheService.saveTache(tache);
        assertSame(tache, resultat);
        verify(tacheRepository).save(tache);
    }
    @Test
    void saveTacheParUtilisateur_doitAssocierUtilisateurEtStatutAFaire() {
        Tache tache = new Tache();
        tache.setTitre("Tache 1");
        tache.setId(5);
        Utilisateur utilisateur = new Utilisateur();
        Statut statutAFaire = new Statut();

        when(utilisateurRepository.findByEmail("wafa@test.com"))
                .thenReturn(Optional.of(utilisateur));
        when(statutRepository.findByLibelle("A faire"))
                .thenReturn(Optional.of(statutAFaire));
        when(tacheRepository.save(tache)).thenReturn(tache);

        Tache resultat = tacheService.saveTacheParUtilisateur(tache, "wafa@test.com");

        assertAll(
                () -> assertSame(utilisateur, resultat.getUtilisateur()),
                () -> assertSame(statutAFaire, resultat.getStatut()),
                () -> assertEquals("Tache 1", resultat.getTitre()),
                () -> assertEquals(0, resultat.getId())
        );
        verify(tacheRepository).save(tache);
    }

    @Test
    void saveTacheParUtilisateur_doitLeverUneExceptionSiUtilisateurIntrouvable() {
        Tache tache = new Tache();
        when(utilisateurRepository.findByEmail("inconnu@test.com"))
                .thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> tacheService.saveTacheParUtilisateur(tache, "inconnu@test.com"));

        assertEquals("Utilisateur introuvable", e.getMessage());
        verify(tacheRepository, never()).save(any());
    }

    @Test
    void saveTacheParUtilisateur_doitLeverUneExceptionSiStatutAFaireIntrouvable() {
        Tache tache = new Tache();
        when(utilisateurRepository.findByEmail("wafa@test.com"))
                .thenReturn(Optional.of(new Utilisateur()));
        when(statutRepository.findByLibelle("A faire"))
                .thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> tacheService.saveTacheParUtilisateur(tache, "wafa@test.com"));

        assertEquals("Statut À faire introuvable", e.getMessage());
        verify(tacheRepository, never()).save(any());
    }

    @Test
    void deleteTache_doitAppelerLeRepositoryAvecLId() {
        tacheService.deleteTache(1);
        verify(tacheRepository).deleteById(1);
    }

    @Test
    void deleteTacheByIdAndEmail_doitSupprimerLaTacheTrouvee() {
        Tache tache = new Tache();
        when(tacheRepository.findByUtilisateurEmailAndId("wafa@test.com", 1))
                .thenReturn(Optional.of(tache));

        tacheService.deleteTacheByIdAndEmail("wafa@test.com", 1);

        verify(tacheRepository).delete(tache);
    }

    @Test
    void deleteTacheByIdAndEmail_doitLeverUneExceptionSiTacheIntrouvable() {
        when(tacheRepository.findByUtilisateurEmailAndId("wafa@test.com", 99))
                .thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> tacheService.deleteTacheByIdAndEmail("wafa@test.com", 99));

        assertEquals("Tâche introuvable", e.getMessage());//le service signale l’erreur
        verify(tacheRepository, never()).delete(any()); //le service n’a rien supprimé
    }
    @Test
    void modifierStatut_doitModifierEtSauvegarderTache() {
        Statut ancienStatut = new Statut();
        Statut nouveauStatut = new Statut();
        Tache tache = new Tache();
        tache.setStatut(ancienStatut);

        when(tacheRepository.findByUtilisateurEmailAndId("wafa@test.com", 99))
                .thenReturn(Optional.of(tache));
        when(statutRepository.findById(2))
                .thenReturn(Optional.of(nouveauStatut));
        when(tacheRepository.save(tache)).thenReturn(tache);

        Tache resultat = tacheService.modifierStatut(99, 2, "wafa@test.com");

        assertAll(
                () -> assertSame(tache, resultat),
                () -> assertSame(nouveauStatut, resultat.getStatut())
        );
        verify(tacheRepository).save(tache);
    }

    @Test
    void modifierStatut_doitLeverUneExceptionSiTacheIntrouvable() {
        when(tacheRepository.findByUtilisateurEmailAndId("wafa@test.com", 99))
                .thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> tacheService.modifierStatut(99, 1, "wafa@test.com"));

        assertEquals("Tâche introuvable", e.getMessage());
        verify(tacheRepository, never()).save(any());
    }
    @Test
    void modifierStatut_doitLeverUneExceptionSiStatutIntrouvable() {
        Tache tache = new Tache();
        when(tacheRepository.findByUtilisateurEmailAndId("wafa@test.com", 99))
                .thenReturn(Optional.of(tache));
        when (statutRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> tacheService.modifierStatut(99, 1, "wafa@test.com"));

        assertEquals("Statut introuvable", e.getMessage());
        verify(tacheRepository, never()).save(any());
    }
}