package com.example.demo.src.goods;

import com.example.demo.config.BaseException;
import com.example.demo.src.goods.model.PatchGoodsReq;
import com.example.demo.src.goods.model.PictureGoods;
import com.example.demo.src.goods.model.PostGoodsReq;
import com.example.demo.src.goods.model.Tag;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


// Service Create, Update, Delete 의 로직 처리
@Service
public class GoodsService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoodsDao goodsDao;
    private final GoodsProvider goodsProvider;
    private final JwtService jwtService;


    @Autowired
    public GoodsService(GoodsDao goodsDao, GoodsProvider goodsProvider, JwtService jwtService) {
        this.goodsDao = goodsDao;
        this.goodsProvider = goodsProvider;
        this.jwtService = jwtService;

    }

    public void postViews(String myId, String goodsId) throws BaseException {
        try {
            goodsDao.postViews(myId, goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int postGoods(String myId, PostGoodsReq postGoodsReq) throws BaseException {
        try {
            return goodsDao.postGoods(myId, postGoodsReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void postWishList(String goodsId) throws BaseException {
        try {
            goodsDao.postWishList(goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void postGoodsImg(String goodsId, PictureGoods imgUrl, String isFirst) throws BaseException {
        try {
            goodsDao.postGoodsImg(goodsId, imgUrl, isFirst);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void postGoodsTag(String goodsId, Tag tag) throws BaseException {
        try {
            goodsDao.postGoodsTag(goodsId, tag);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void patchGoods(String goodsId, PatchGoodsReq patchGoodsReq) throws BaseException {
        try {
            int result = goodsDao.patchGoods(goodsId, patchGoodsReq);
            if(result == 0) {
                throw new BaseException(MODIFY_FAIL_GOODS);
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void postKeyword(String keyword, String myId) throws BaseException {
        try{
            goodsDao.postKeyword(keyword, myId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchGoodsTag(String goodsId) throws BaseException {
        try {
            goodsDao.patchGoodsTag(goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void patchGoodsImg(String goodsId) throws BaseException {
        try {
            goodsDao.patchGoodsImg(goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteGoods(String goodsId) throws BaseException {
        try {
            goodsDao.deleteGoods(goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
