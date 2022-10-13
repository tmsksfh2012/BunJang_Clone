package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatRooms {
    private int crId;
    private String createdAt;
    private String updatedAt;
    private String status;
    private int buyerId;
    private String buyerAlarm;
    private int sellerId;
    private String sellerAlarm;
    private int goodsId;
}
