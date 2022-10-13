package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostMakeDealRes {
    private int thId;
    private String title;
    private String message;

    private String goodsTitle;
    private String method;
    private String askPrice;
}
