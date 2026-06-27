package com.tm.repository;

import com.tm.entity.CardSynergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardSynergyRepository extends JpaRepository<CardSynergyEntity, Long> {
    List<CardSynergyEntity> findByRequiredTagIn(List<String> tags);
}
