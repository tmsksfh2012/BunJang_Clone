package com.example.demo.src.user.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GetUserDetailRes {
    @NonNull private String nickName;
    @NonNull private String imgUrl;
    @NonNull private String content;
    private float star;
    private int transaction;
    @NonNull private int follower;
    @NonNull private int following;
    //private int secureTransaction;
    @NonNull private int gapCreatedAt;
    //private String isAuthMyself;
}
