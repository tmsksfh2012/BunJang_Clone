package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostChatRes {
    private int chatId;
    private String content;
    private int fromId;
    private String type;
}
