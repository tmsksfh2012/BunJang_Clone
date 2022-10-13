package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class
PostChatRoomsReq {
    private int buyerId;
    private int sellerId;
    private int goodsId;
}
