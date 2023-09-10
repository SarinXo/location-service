package com.example.locationservice.repository;

import com.example.locationservice.entity.Geodata;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GeodataRepository extends CrudRepository<Geodata, Integer> {
    Optional<Geodata> findByName(String name);
}
