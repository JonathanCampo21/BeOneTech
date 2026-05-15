package com.frontline.frontline_tech.repository;

import com.frontline.frontline_tech.model.Louvor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LouvorRepository extends JpaRepository<Louvor, Long> {
}