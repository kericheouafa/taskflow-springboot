package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.model.Tache;
import org.aspectj.weaver.ast.And;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Integer> {
    List<Tache> findByUtilisateurEmail(String email);
    Optional<Tache> findByUtilisateurEmailAndId(String email, Integer id);
    Optional<Tache> findById(Integer integer);
}