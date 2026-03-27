package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.model.Statut;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutRepository extends JpaRepository<Statut, Integer> {
}