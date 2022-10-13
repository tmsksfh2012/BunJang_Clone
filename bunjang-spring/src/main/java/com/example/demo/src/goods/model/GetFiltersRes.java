package com.example.demo.src.goods.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFiltersRes {
    private List<Brand> brandList;
    private List<Category> categoryList;
}
