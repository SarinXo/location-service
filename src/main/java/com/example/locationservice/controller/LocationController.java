package com.example.locationservice.controller;

import com.example.locationservice.model.Weather.submodules.Coord;
import com.example.locationservice.serivice.GeodataService;
import com.example.locationservice.entity.Geodata;
import com.example.locationservice.model.Weather.Weather;
import org.bouncycastle.oer.its.etsi102941.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class LocationController {

    private final String WEATHER_URL;

    private final GeodataService service;

    private final RestTemplate externalRestTemplate;

    @Autowired
    public LocationController(GeodataService service,
                              @Value("${url.services.weather}") String weatherUrl,
                              RestTemplate externalRestTemplate) {
        this.service = service;
        this.externalRestTemplate = externalRestTemplate;
        WEATHER_URL = weatherUrl;
    }

    @GetMapping("/weather")
    public ResponseEntity<Weather> redirectRequestToWeather(@RequestParam String location) {
        try{
            Optional<Geodata> geodata = service.findByName(location);
            Geodata eGeodata = geodata.get();
            URI url = new URI(String.format("%s/weather/lat=%s&lon=%s",
                    WEATHER_URL, eGeodata.getLat(), eGeodata.getLon()));
            return externalRestTemplate.getForEntity(
                    url,
                    Weather.class);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (RestClientException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/geodata")
    public ResponseEntity<Geodata> getWeather(@RequestParam String location) {
        Optional<Geodata> geodata = service.findByName(location);
        return geodata.isPresent()
                ? new ResponseEntity<>(geodata.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/geodata")
    public ResponseEntity<?> save(@RequestBody Geodata geodata) {
        Optional<Geodata> resultPushGeodata = service.pushInDb(geodata);
        return resultPushGeodata.isPresent()
                ? new ResponseEntity<>(resultPushGeodata, HttpStatus.CREATED)
                : new ResponseEntity<>("Данный пользователь уже есть в базе данных", HttpStatus.BAD_REQUEST);
    }
}
