package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatsRes {
    private String messageTime;
    private String nickName;
    private int fromId;
    private String status;
    private String imgUrl;
    private int chatsId;
    private String content;
    private String type;
    private String isChecked;
}
