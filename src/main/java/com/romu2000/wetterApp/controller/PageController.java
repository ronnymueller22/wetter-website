package com.romu2000.wetterApp.controller;

import com.romu2000.wetterApp.model.LngLatModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
public class PageController {


    LngLatModel wetterStadt = new LngLatModel();

    @GetMapping("/auswahlStadt")
    public String auswahlStadt(Model model) throws IOException, InterruptedException {
        if(wetterStadt.getMyStadt() != null){
            try{
                berechneStadt();
                wetterDetails();
                model.addAttribute("ausgabe", "In " +wetterStadt.getMyStadt() + " hat es " +wetterStadt.getTemp() + " Grad Celsius und es " + stringWetterCode(wetterStadt.getWeathercode()));
                model.addAttribute("stadt", wetterStadt.getMyStadt());
                model.addAttribute("lat", wetterStadt.getLat());
                model.addAttribute("lng", wetterStadt.getLat());
                model.addAttribute("error","");
            }catch(Exception e){
                model.addAttribute("ausgabe","Upppps, ein Fehler ist aufgetreten....");
            }
            return "auswahlStadt";
        }else{
            return null;
        }
    }
       @PostMapping("/auswahlStadt")
       public String myStadt(@RequestParam String testName){
        LngLatModel addStadt = new LngLatModel();
        wetterStadt.setMyStadt(testName);
        return "redirect:/auswahlStadt";
       }

       public void berechneStadt() throws IOException, InterruptedException {

           HttpClient client = HttpClient.newHttpClient();
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create("https://maps.googleapis.com/maps/api/geocode/json?address=" + wetterStadt.getMyStadt() + "&key=AIzaSyBs3AMqs8KkY-iyJuTcVAJtOidZBQawx_Q"))
                   .build();
           HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
           System.out.println("TEST: " + response);
           JSONObject stadtZuLngLat = new JSONObject(response.body());
           JSONArray results = stadtZuLngLat.getJSONArray("results");
           JSONObject nuller = results.getJSONObject(0);
           JSONObject geometry =  nuller.getJSONObject("geometry");
           JSONObject location = geometry.getJSONObject("location");
           BigDecimal lng = (BigDecimal) location.get("lng");
           BigDecimal lat = (BigDecimal) location.get("lat");
           // Objekt füllen
           wetterStadt.setLat(lat);
           wetterStadt.setLng(lng);
       }
       public void wetterDetails() throws IOException, InterruptedException {
           HttpClient client = HttpClient.newHttpClient();
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create("https://api.open-meteo.com/v1/forecast?latitude=" + wetterStadt.getLat() + "&longitude=" + wetterStadt.getLng() + "&current_weather=true&hourly=temperature_2m,relativehumidity_2m,windspeed_10m"))
                   .build();
           HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
           System.out.println(response);
           JSONObject wetterDaten = new JSONObject(response.body());
           JSONObject current_weather = wetterDaten.getJSONObject("current_weather");
           BigDecimal temperature = (BigDecimal) current_weather.get("temperature");
           Integer weathercode = (Integer) current_weather.get("weathercode");
           // Objekt füllen
           wetterStadt.setTemp(temperature);
           wetterStadt.setWeathercode(weathercode);
           System.out.println(weathercode + " WETTERCODE");
       }


       public String stringWetterCode(Integer weathercode){
        String wetterCode = "";
        if(weathercode >= 0 && weathercode < 10){
            wetterCode = "ist wolkenlos";
        }
           if(weathercode >= 10 && weathercode < 20){
               wetterCode = "ist neblig";
           }
           if(weathercode >= 20 && weathercode < 30){
               wetterCode = "hat leichte Schauer";
           }
           if(weathercode >= 30 && weathercode < 40){
               wetterCode = "hat leichte Schneestürme";
           }
           if(weathercode >= 40 && weathercode < 50){
               wetterCode = "ist stark neblig";
           }
           if(weathercode >= 50 && weathercode < 60){
               wetterCode = "hagelt";
           }
           if(weathercode >= 60 && weathercode < 70){
               wetterCode = "regnet";
           }
           if(weathercode >= 70 && weathercode < 80){
               wetterCode = "schneit";
           }
           if(weathercode >= 80 && weathercode < 90){
               wetterCode = "hat leichte Regenschauer";
           }
           if(weathercode >= 90 && weathercode < 100){
               wetterCode = "gewittert";
           }
        return wetterCode;
       }
}
