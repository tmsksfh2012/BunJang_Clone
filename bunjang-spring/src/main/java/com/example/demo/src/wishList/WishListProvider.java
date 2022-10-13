package com.example.demo.src.wishList;

import com.example.demo.config.BaseException;
import com.example.demo.src.wishList.model.GetWishListRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class WishListProvider {
    private final WishListDao wishListDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public WishListProvider(WishListDao wishListDao, JwtService jwtService) {
        this.wishListDao = wishListDao;
        this.jwtService = jwtService;
    }


    public int checkGoodsId(String goodsId) throws BaseException {
        try {
            return wishListDao.checkGoodsId(goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkGoodsUserId(String userId, String goodsId) throws BaseException {
        try {
            return wishListDao.checkGoodsUserId(userId, goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkUserStatusByUserId(String userId) throws BaseException {
        try {
            return wishListDao.checkUserStatusByUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkWishStatusByUserId(String myId, String goodsId) throws BaseException {
        try {
            return wishListDao.checkWishStatusByUserId(myId, goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetWishListRes> getWishList(String myId) throws BaseException {
        try {
            return wishListDao.getWishList(myId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public List<GetGoodsRes> getWishListMy(String myId) throws BaseException {
//        try {
//            return wishListDao.getWishListMy(myId);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
}
