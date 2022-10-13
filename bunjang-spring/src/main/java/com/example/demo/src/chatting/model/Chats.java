package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Chats {
    private int chatsId;
    private int crId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private String content;
    private int fromId;
    private int toId;
    private String type;
    private String isChecked;
}
