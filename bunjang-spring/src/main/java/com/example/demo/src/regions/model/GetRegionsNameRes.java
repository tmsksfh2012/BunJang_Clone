package com.example.demo.src.regions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRegionsNameRes {
    private int regionId;
    private String provinceName;
    private String districtName;
    private String regionName;

}
