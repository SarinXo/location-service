package com.example.locationservice;

import com.example.locationservice.entity.Geodata;
import com.example.locationservice.repository.GeodataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeodataService {

    private final GeodataRepository repository;

    @Autowired
    public GeodataService(GeodataRepository repository) {
        this.repository = repository;
    }

    public Optional<Geodata> findByName(String location) {
        return repository.findByName(location);
    }

    public Geodata pushInDb(Geodata geodata) {
        Optional<Geodata> findGeodata = repository.findById(geodata.getId());
        if(findGeodata.isEmpty())
            repository.save(geodata);
        return findGeodata.orElse(geodata);
    }

}
