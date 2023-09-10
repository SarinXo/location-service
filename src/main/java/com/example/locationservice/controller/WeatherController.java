package com.example.locationservice.controller;

import com.example.locationservice.entity.Geodata;
import com.example.locationservice.model.Weather.Weather;
import com.example.locationservice.repository.GeodataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class WeatherController {

    private final GeodataService repository;
    @Autowired
    public WeatherController(GeodataService service) {
        this.service = service;
    }

    @GetMapping("/weather")
    public Weather redirectRequestWeather(@RequestParam String location) {
        Geodata geodata = repository.findByName(location).get();
        String url = String.format("http://localhost:8082/?lat=%s&lon=%s", geodata.getLat(), geodata.getLon());
        return restTemplate.getForObject(url, Weather.class);
    }

    @GetMapping("/geodata")
    public ResponseEntity<Geodata> getWeather(@RequestParam String location) {
        Optional<Geodata> geodata = repository.findByName(location);
        return geodata.isPresent()
                ? new ResponseEntity<>(geodata.get(), HttpStatus.OK)
                : new ResponseEntity<>(new Geodata(), HttpStatus.NOT_FOUND);
    }
    @PostMapping("/geodata")
    public ResponseEntity<Geodata> save(@RequestBody Geodata geodata) {
        return repository.save(geodata);
    }
}
