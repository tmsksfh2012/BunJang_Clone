package com.example.demo.src.goods.model;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchGoodsReq {
    private String title;
    private String content;
    private Integer price;
    private Integer categoryId;
    private Integer brandId;
    private List<Tag> tagList;
    private String isFreeShipping; //배송비포함여부 //can null
    private Integer numberOfGoods;
    private String isUserGoods; //can null
    private String canExchange; //can null
    private String isAdvertising; //can null
    private String canBungaePay; //can null
    private Integer regionId; //지역명 //can null
    private List<PictureGoods> goodsImgList; //이미지 리스트
}