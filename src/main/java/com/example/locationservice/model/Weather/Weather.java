package com.example.locationservice.model.Weather;

import com.example.locationservice.model.Weather.submodules.Clouds;
import com.example.locationservice.model.Weather.submodules.Coord;
import com.example.locationservice.model.Weather.submodules.Main;
import com.example.locationservice.model.Weather.submodules.Sys;
import com.example.locationservice.model.Weather.submodules.Wind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Weather {
    private Coord coord;
    private ArrayList<com.example.locationservice.model.Weather.submodules.Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
}
