package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRoomsRes {
    private int crId;
    private String roomImgUrl;
    private String roomName;
    private int chatsId;
    private int goodsId;
    private String thumbnail;
    private String lastMessageTime;
    private String chatContent;
}
