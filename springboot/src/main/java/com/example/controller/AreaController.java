package com.example.controller;

import com.example.common.Result;
import com.example.entity.Area;
import com.example.service.AreaService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/area")
public class AreaController {

    @Resource
    private AreaService areaService;



    @Autowired
    private AreaService areaMapper;

    @GetMapping("/provinces")
    public List<String> getProvinces() {
        return areaMapper.findAllProvinces();
    }

    @GetMapping("/cities")
    public List<String> getCitiesByProvince(@RequestParam String province) {
        return areaMapper.findCitiesByProvince(province);
    }

    @GetMapping("/districts")
    public List<String> getDistrictsByCity(@RequestParam String city) {
        return areaMapper.findDistrictsByCity(city);
    }



}