package com.example.mapper;

import com.example.entity.Area;

import java.util.List;

public interface AreaMapper {
    List<String> findAllProvinces();
    List<String> findCitiesByProvince(String province);
    List<String> findDistrictsByCity(String city);
}
