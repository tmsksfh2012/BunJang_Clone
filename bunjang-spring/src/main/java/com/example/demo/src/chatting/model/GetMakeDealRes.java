package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMakeDealRes {
    private int thId;
    private int goodsId;
    private String goodsTitle;
    private String goodsImg;
    private String isFreeShipping;
    private String askPrice;
    private String meetAt;
    private String regionContent;
    private String email;
}
