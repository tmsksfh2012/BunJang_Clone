package com.example.demo.src.following.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GetFollowingRes {
    @NonNull private int userId;
    @NonNull private String nickName;
    @NonNull private String userImgUrl;
    @NonNull private int countGoods;
    @NonNull private int countFollowing;
    private List<FollowingGoods> goodsList;
}
