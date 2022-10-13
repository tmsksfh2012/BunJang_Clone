package com.example.demo.src.goods.model;

import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Goods {
        @NonNull private int goodsId;
        @NonNull private String title;
        @NonNull private String content;
        @NonNull private String seller; //판매자명
        @NonNull private String price;
        @NonNull private int categoryId;
        @NonNull private String isFreeShipping;
        @NonNull private int numberOfGoods;
        @NonNull private String isUserGoods;
        @NonNull private String canExchange;
        @NonNull private String isAdvertising;
        @NonNull private String canBungaePay;
        @NonNull private String region; //지역명
        @NonNull private String status; //상태
        @NonNull private int likes; //찜 개수
        @NonNull private String isLike; //찜여부
        private int views; //조회수
        private List<PictureGoodsInfo> goodsImgList; //이미지 리스트
        private List<GetTag> tagList; //태그 리스트
}
