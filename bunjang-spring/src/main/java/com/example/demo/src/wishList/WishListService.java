package com.example.demo.src.wishList;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class WishListService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final WishListDao wishListDao;
    private final WishListProvider wishListProvider;
    private final JwtService jwtService;


    @Autowired
    public WishListService(WishListDao wishListDao, WishListProvider wishListProvider, JwtService jwtService) {
        this.wishListDao = wishListDao;
        this.wishListProvider = wishListProvider;
        this.jwtService = jwtService;
    }

    public void postWishList(String myId, String goodsId) throws BaseException {
        try {
            wishListDao.postWishList(myId, goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void deleteWishList(String myId, String goodsId) throws BaseException {
        try {
            wishListDao.deleteWishList(myId, goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
