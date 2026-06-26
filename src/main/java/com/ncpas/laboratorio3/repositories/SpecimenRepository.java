package com.ncpas.laboratorio3.repositories;

import com.ncpas.laboratorio3.domain.entities.Specimen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpecimenRepository extends JpaRepository<Specimen, UUID> {
}