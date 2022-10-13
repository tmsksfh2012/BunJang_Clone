package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRoomsInfoReq {
    private String nickName;
    private String goodsImg;
    private String goodsPrice;
    private String goodsTitle;
    private String goodsCanBungaePay;
}
