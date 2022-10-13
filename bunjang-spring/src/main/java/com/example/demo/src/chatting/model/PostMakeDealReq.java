package com.example.demo.src.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostMakeDealReq {
    private Integer askPrice;
    private String meetAt;
    private String regionContent;
}
