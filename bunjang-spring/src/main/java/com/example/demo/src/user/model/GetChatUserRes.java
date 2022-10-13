package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatUserRes {
    private String nickName;
    private String userImg;
    private String rating;
    private String countOfReview;
    private String transactionHistory;
    private String goodsTitle;
    private String goodsPrice;
    private String goodsImg;
}
