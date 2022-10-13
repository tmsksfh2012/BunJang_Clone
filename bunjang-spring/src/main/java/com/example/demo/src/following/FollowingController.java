package com.example.demo.src.following;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.following.model.GetFollowerRes;
import com.example.demo.src.following.model.GetFollowingRes;
import com.example.demo.src.following.model.PostFollowingReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;

@RestController
@RequestMapping("/following")
public class FollowingController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FollowingProvider followingProvider;
    @Autowired
    private final FollowingService followingService;
    @Autowired
    private final JwtService jwtService;

    public FollowingController(FollowingProvider followingProvider, FollowingService followingService, JwtService jwtService){
        this.followingProvider = followingProvider;
        this.followingService = followingService;
        this.jwtService = jwtService;
    }

    /**
     * 팔로우하기 API
     * [POST] /following
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postFollowing(@RequestBody PostFollowingReq postFollowingReq) {
        if(postFollowingReq == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        if (!isRegexInteger(postFollowingReq.getUserId())) {
            return new BaseResponse<>(INVALID_BODY);
        }
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (followingProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if (followingProvider.checkUserStatusByUserId(postFollowingReq.getUserId()) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }
            if (followingProvider.checkFollowingStatusByUserId(userIdByJwt, postFollowingReq.getUserId()) == 1) {
                return new BaseResponse<>(POST_ALREADY_EXIST_FOLLOWING);
            }

            followingService.postFollowing(userIdByJwt, postFollowingReq.getUserId());

            return new BaseResponse<>("success");

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팔로우 삭제 API
     * [POST] /following
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> patchFollowing(@RequestBody PostFollowingReq deleteFollowingReq) {
        if(deleteFollowingReq == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        if (!isRegexInteger(deleteFollowingReq.getUserId())) {
            return new BaseResponse<>(INVALID_BODY);
        }
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (followingProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if (followingProvider.checkUserStatusByUserId(deleteFollowingReq.getUserId()) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }
            if (followingProvider.checkFollowingStatusByUserId(userIdByJwt, deleteFollowingReq.getUserId()) == 0) {
                return new BaseResponse<>(POST_NO_EXIST_FOLLOWING);
            }

            followingService.deleteFollowing(userIdByJwt, deleteFollowingReq.getUserId());

            return new BaseResponse<>("success");

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팔로잉 조회 API
     * [POST] /following/followee/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/followee/{userId}")
    public BaseResponse<List<GetFollowingRes>> getFollowee(@PathVariable(required = false) String userId) {

        if (userId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        if (!isRegexInteger(userId)) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (followingProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetFollowingRes> getFollowingRes = followingProvider.getFolloweeRes(userId);

            if(getFollowingRes.isEmpty()) {
                return new BaseResponse<>(GET_FOLLOWING_LIST_NO_EXIST);
            }

            for (GetFollowingRes getFollowingRe : getFollowingRes) {
                String user_id = String.valueOf(getFollowingRe.getUserId());
                getFollowingRe.setGoodsList(followingProvider.getFolloweeGoods(user_id));
            }

            return new BaseResponse<>(getFollowingRes);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팔로워 조회 API
     * [GET] /following/follower/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/follower/{userId}")
    public BaseResponse<List<GetFollowerRes>> getFollower(@PathVariable(required = false) String userId) {

        if (userId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        if (!isRegexInteger(userId)) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (followingProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetFollowerRes> res =  followingProvider.getFollowerRes(userId);

            if(res.isEmpty()) {
                return new BaseResponse<>(GET_FOLLOWING_LIST_NO_EXIST);
            }

            return new BaseResponse<>(res);


        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
