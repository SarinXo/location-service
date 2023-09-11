package com.example.locationservice.controller;

import com.example.locationservice.GeodataService;
import com.example.locationservice.entity.Geodata;
import com.example.locationservice.model.Weather.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class WeatherController {

    private final GeodataService service;

    private final RestTemplate restTemplate;
    @Autowired
    public WeatherController(GeodataService service,
                             RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/weather")
    public ResponseEntity<Weather> redirectRequestWeather(@RequestParam String location) {
        Optional<Geodata> geodata = service.findByName(location);
        try{
            Geodata presentGeodata = geodata.get();
            String url = String.format("http://localhost:8082/?lat=%s&lon=%s",
                        presentGeodata.getLat(), presentGeodata.getLon());
            return restTemplate.getForEntity(url, Weather.class);
        }catch (RestClientException | NoSuchElementException e ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/geodata")
    public ResponseEntity<Geodata> getWeather(@RequestParam String location) {
        Optional<Geodata> geodata = service.findByName(location);
        return geodata.isPresent()
                ? new ResponseEntity<>(geodata.get(), HttpStatus.OK)
                : new ResponseEntity<>(new Geodata(), HttpStatus.NOT_FOUND);
    }
    @PostMapping("/geodata")
    public ResponseEntity<Geodata> save(@RequestBody Geodata geodata) {
        Geodata resultPushGeodata = service.pushInDb(geodata);
        return resultPushGeodata.equals(geodata)
                ? new ResponseEntity<>(service.findByName(geodata.getName()).get(), HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(resultPushGeodata, HttpStatus.CREATED);
    }
}
