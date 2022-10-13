package com.example.demo.src.wishList.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetWishListRes {
    private int goodsId;
    private String title;
    private String price;
    private String canBungaePay;
    private String imgUrl;
    private String status;
    private String sellerNickName;
    private String sellerImg;
}
