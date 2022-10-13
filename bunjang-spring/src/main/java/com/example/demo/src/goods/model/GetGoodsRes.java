package com.example.demo.src.goods.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetGoodsRes {
    private int goodsId;
    private String title;
    private String price;
    private int likes;
    private String canBungaePay;
    private String imgUrl;
    private String isLike;
    private String status;
}
