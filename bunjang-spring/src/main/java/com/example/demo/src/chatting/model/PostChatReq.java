package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostChatReq {
    private int crId;
    private String content;
    private int fromId;
    private int toId;
    private String type;
}
