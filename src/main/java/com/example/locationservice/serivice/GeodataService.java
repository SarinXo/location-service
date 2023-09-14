package com.example.locationservice.serivice;

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

    public Optional<Geodata> pushInDb(Geodata geodata) {
        Optional<Geodata> findGeodata = repository.findByName(geodata.getName());
        if(findGeodata.isEmpty())
            return Optional.of(repository.save(geodata));
        return Optional.empty();
    }

}
