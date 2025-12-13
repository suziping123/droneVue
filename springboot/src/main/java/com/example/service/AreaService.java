package com.example.service;

import com.example.entity.Area;
import com.example.mapper.AreaMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {

    @Resource
    private AreaMapper areaMapper;

    public List<String> findAllProvinces() {
        return areaMapper.findAllProvinces();
    }

    public List<String> findCitiesByProvince(String province) {
        return areaMapper.findCitiesByProvince(province);
    }

    public List<String> findDistrictsByCity(String city) {
        return areaMapper.findDistrictsByCity(city);
    }
}