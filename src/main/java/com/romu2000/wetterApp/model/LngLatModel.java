package com.romu2000.wetterApp.model;

import java.math.BigDecimal;


public class LngLatModel {
    BigDecimal lat;
    BigDecimal lng;
    BigDecimal temp;
    Integer weathercode;

    public Integer getWeathercode() {
        return weathercode;
    }

    public void setWeathercode(Integer weathercode) {
        this.weathercode = weathercode;
    }

    public BigDecimal getTemp() {
        return temp;
    }

    public void setTemp(BigDecimal temp) {
        this.temp = temp;
    }

    String myStadt;

    public String getMyStadt() {
        return myStadt;
    }

    public void setMyStadt(String myStadt) {
        this.myStadt = myStadt;
    }

    public LngLatModel(Double lat, Double lng) {

    }

    public LngLatModel(){

    }

    public BigDecimal getLat() {

        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }
}
