package com.example.demo.src.regions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetNearRegionsListRes {
    private List<Integer> range2km;
    private List<Integer> range4km;
    private List<Integer> range6km;
    private List<Integer> range8km;
}
