package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostRegisterReq {
//    private String name;
    private String nickName;
    //private String type; #통신사
//    private String email;
    private String token;
}
