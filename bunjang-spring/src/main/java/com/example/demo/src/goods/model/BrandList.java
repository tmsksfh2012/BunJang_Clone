package com.example.demo.src.goods.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BrandList {
    private int brandId;
    private String brandName;
    private String brandEngName;
    private String imgUrl;
    private int counting;
    private String isLike;
}
