package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostChatRoomsRes {
    private int crId;
    private int buyerId;
    private int sellerId;
    private int goodsId;
    private String nickName;
    private String userImg;
    private String rating;
    private String countOfReview;
    private String transactionHistory;
    private String goodsTitle;
    private String goodsPrice;
    private String goodsImg;
}
