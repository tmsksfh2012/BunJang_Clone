package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Objects;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 로그인 API - 비회원용
     * [POST] /users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> loginUser(@RequestBody PostLoginReq postLoginReq) {
        try{
            if(postLoginReq == null) {
                return new BaseResponse<>(EMPTY_BODY);
            }
            //카카오 로그인
            if(postLoginReq.getToken() == null){
                return new BaseResponse<>(USERS_TOKEN_EMPTY);
            }

            PostLoginRes postLoginRes = userService.loginKakaoUser(postLoginReq);
            return new BaseResponse<>(postLoginRes);
//            }
//
//            return new BaseResponse<>(USERS_TYPE_ERROR_TYPE);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 회원가입 API - 비회원용
     * [POST] /users/register
     * @return BaseResponse<PostRegisterRes>
     */
    @ResponseBody
    @PostMapping("/register")
    public BaseResponse<PostRegisterRes> createUser(@RequestBody PostRegisterReq postRegisterReq) {
        try{
            System.out.println("회원가입 시도");
            if(postRegisterReq == null) {
                return new BaseResponse<>(EMPTY_BODY);
            }
            //카카오 로그인
            if(postRegisterReq.getToken() == null){
                return new BaseResponse<>(USERS_TOKEN_EMPTY);
            }

            if(postRegisterReq.getNickName() == null){
                return new BaseResponse<>(NICKNAME_CAN_NOT_EMPTY);
            }

            PostRegisterRes postRegisterRes = userService.createKakaoUser(postRegisterReq);
            return new BaseResponse<>(postRegisterRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 자동 로그인 API - 비회원용
     * [POST] /users/auto-login
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/auto-login")
    public BaseResponse<String> autoLoginUser(@RequestBody PostAutoLoginReq postAutoLoginReq) {
        try{
            if(postAutoLoginReq == null) {
                return new BaseResponse<>(EMPTY_BODY);
            }
            if(postAutoLoginReq.getUserId() == null){
                return new BaseResponse<>(NO_EXIST_USER_ID);
            }
            if(!isRegexInteger(postAutoLoginReq.getUserId())) {
                return new BaseResponse<>(INVALID_BODY);
            }
            String userId = postAutoLoginReq.getUserId();
            if(userProvider.checkUserStatusByUserId(userId) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }

            String userIdByJwt = String.valueOf(jwtService.getUserId());

            if(!Objects.equals(userId, userIdByJwt)) {
                return new BaseResponse<>(INVALID_JWT_TOKEN_USER);
            }


            return new BaseResponse<>("success");

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 내 정보 수정
     * [PATCH] /users
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> patchMy(@RequestBody PatchUserReq patchUserReq) {
        if(patchUserReq.getNickName() == null) {
            return new BaseResponse<>(NICKNAME_CAN_NOT_EMPTY);
        }
        if(patchUserReq.getImgUrl() == null) {
            patchUserReq.setImgUrl("https://guriman.shop/ImgUsers/default.png");
        }
        if(patchUserReq.getContent() == null) {
            patchUserReq.setContent("");
        }
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (userProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            userService.patchMy(userIdByJwt, patchUserReq);

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내 정보 조회
     * [GET] /users
     * @return BaseResponse<GetUserDetailRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetUserDetailRes> getMy() {
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (userProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            GetUserDetailRes getUserRes = userProvider.getUserDetail(userIdByJwt);
            UserDetailStarTransaction getUserStarTransaction = userProvider.getUserDetailStarAndTransaction(userIdByJwt);

            getUserRes.setTransaction(getUserStarTransaction.getTransaction());
            getUserRes.setStar(getUserStarTransaction.getStar());

            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 정보 조회
     * [GET] /users/{userId}
     * @return BaseResponse<GetUserDetailRes>
     */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetUserDetailRes> getUser(@PathVariable(required = false) String userId) {
        if (userId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        if (!isRegexInteger(userId)) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }
        try {
            if (userProvider.checkUserStatusByUserId(userId) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (userProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            GetUserDetailRes getUserRes = userProvider.getUserDetail(userId);
            UserDetailStarTransaction getUserStarTransaction = userProvider.getUserDetailStarAndTransaction(userId);

            getUserRes.setTransaction(getUserStarTransaction.getTransaction());
            getUserRes.setStar(getUserStarTransaction.getStar());

            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 탈퇴
     * [GET] /users/removal
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/removal")
    public BaseResponse<String> deleteUser() {
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (userProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            userService.deleteUser(userIdByJwt);

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 차단 상점 추가 API
     * [POST] /users/banned/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/banned/{userId}")
    public BaseResponse<String> addBanUser(@PathVariable("userId") String userId) {
        if (userId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        if (!isRegexInteger(userId)) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }

        try{
            if(userProvider.checkUserStatusByUserId(userId) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }

            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if(userProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if(userId.equals(userIdByJwt)) {
                return new BaseResponse<>(CAN_NOT_BAN_MYSELF);
            }

            if(userProvider.checkBanUser(userIdByJwt, userId) == 1) {
                return new BaseResponse<>(EXIST_BAN_USER);
            }

            userService.postBanUser(userIdByJwt, userId);

            return new BaseResponse<>("success");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 차단 상점 해제 API
     * [PATCH] /users/banned/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/banned/{userId}")
    public BaseResponse<String> unBanUser(@PathVariable("userId") String userId) {
        if (userId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        if (!isRegexInteger(userId)) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }

        try{
            if(userProvider.checkUserStatusByUserId(userId) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }

            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if(userProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if(userProvider.checkBanUser(userIdByJwt, userId) == 0) {
                return new BaseResponse<>(NO_EXIST_BAN_USER);
            }

            userService.patchBanUser(userIdByJwt, userId);

            return new BaseResponse<>("success");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
