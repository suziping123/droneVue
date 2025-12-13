package com.example.entity;

public class Area {
    private Integer id;
    private String profence;
    private String city;
    private String districts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfence() {
        return profence;
    }

    public void setProfence(String profence) {
        this.profence = profence;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistricts() {
        return districts;
    }

    public void setDistricts(String districts) {
        this.districts = districts;
    }
}