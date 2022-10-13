package com.example.demo.src.wishList;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.wishList.model.GetWishListRes;
import com.example.demo.src.wishList.model.PostWishListReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;

@RestController
@RequestMapping("/wish-list")
public class WishListController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final WishListProvider wishListProvider;
    @Autowired
    private final WishListService wishListService;
    @Autowired
    private final JwtService jwtService;

    public WishListController(WishListProvider wishListProvider, WishListService wishListService, JwtService jwtService){
        this.wishListProvider = wishListProvider;
        this.wishListService = wishListService;
        this.jwtService = jwtService;
    }

    /**
     * 찜목록 조회 API
     * [GET] /wish-list
     * @return BaseResponse<List<GetWishListRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetWishListRes>> getWishList() {
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (wishListProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            List<GetWishListRes> res =  wishListProvider.getWishList(userIdByJwt);

            if(res.isEmpty()) {
                return new BaseResponse<>(GET_GOODS_LIST_NO_EXIST);
            }

            return new BaseResponse<>(res);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 찜하기 API
     * [POST] /wish-list
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postWishList(@RequestBody PostWishListReq goodsId) {
        if(goodsId == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        if(!isRegexInteger(goodsId.getGoodsId())) {
            return new BaseResponse<>(INVALID_BODY);

        }
        try {

            String goods_id = goodsId.getGoodsId();
            if(wishListProvider.checkGoodsId(goods_id) == 0) {
                return new BaseResponse<>(GET_GOODS_NOT_EXIST);
            }
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (wishListProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if(wishListProvider.checkGoodsUserId(userIdByJwt, goods_id) != 0) {
                return new BaseResponse<>(CAN_NOT_LIKE_YOUR_GOODS);
            }
            if (wishListProvider.checkWishStatusByUserId(userIdByJwt, goods_id) == 1) {
                return new BaseResponse<>(POST_ALREADY_EXIST_WISH);
            }
            wishListService.postWishList(userIdByJwt, goods_id);

            return new BaseResponse<>("success");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 찜삭제 API
     * [PATCH] /wish-list
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> deleteWishList(@RequestBody PostWishListReq goodsId) {
        if(goodsId == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        if(goodsId.getGoodsId().equals("")) {
            return new BaseResponse<>(NO_EXIST_GOODS_ID);
        }
        try {

            String goods_id = goodsId.getGoodsId();
            if(wishListProvider.checkGoodsId(goods_id) == 0) {
                return new BaseResponse<>(GET_GOODS_NOT_EXIST);
            }
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (wishListProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            if (wishListProvider.checkWishStatusByUserId(userIdByJwt, goods_id) == 0) {
                return new BaseResponse<>(POST_NO_EXIST_WISH);
            }
            wishListService.deleteWishList(userIdByJwt, goods_id);

            return new BaseResponse<>("success");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
