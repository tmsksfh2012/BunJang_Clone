package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetRoomsInfoRes {
    private String nickName;
    private String goodsImg;
    private String goodsPrice;
    private String goodsTitle;
    private String goodsCanBungaePay;
    private List<GetChatsRes> getChatsResList;
}
