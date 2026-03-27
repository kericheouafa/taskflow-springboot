package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.model.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Integer> {
}