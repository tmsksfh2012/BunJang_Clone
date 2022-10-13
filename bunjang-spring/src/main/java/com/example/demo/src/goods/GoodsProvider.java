package com.example.demo.src.goods;

import com.example.demo.config.BaseException;
import com.example.demo.src.goods.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class GoodsProvider {
    private final GoodsDao goodsDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public GoodsProvider(GoodsDao goodsDao, JwtService jwtService) {
        this.goodsDao = goodsDao;
        this.jwtService = jwtService;
    }

    public List<GetGoodsRes> getGoodsHomeList(String myId) throws BaseException {
        try {
            return goodsDao.getGoodsHomeList(myId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<BrandList> getGoodsBrandList(String userId) throws BaseException {
        try {
            return goodsDao.getGoodsBrandList(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<CategoryList> getGoodsCategoryList() throws BaseException {
        try {
            return goodsDao.getGoodsCategoryList();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsMy(String myId) throws BaseException {
        try {
            return goodsDao.getGoodsMy(myId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsUser(String myId, String userId) throws BaseException {
        try {
            return goodsDao.getGoodsUser(myId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsUserOrder(String myId, String userId, int orderId) throws BaseException {
        try {
            return goodsDao.getGoodsUserOrder(myId, userId, orderId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsUserCategory(String myId, String userId, int categoryId) throws BaseException {
        try {
            return goodsDao.getGoodsUserCategory(myId, userId, categoryId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsUserBrand(String myId, String userId, int brandId) throws BaseException {
        try {
            return goodsDao.getGoodsUserBrand(myId, userId, brandId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsUserCategoryBrand(String myId, String userId, int categoryId, int brandId) throws BaseException {
        try {
            return goodsDao.getGoodsUserCategoryBrand(myId, userId, categoryId, brandId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsUserCategoryOrder(String myId, String userId, int categoryId, int orderId) throws BaseException {
        try {
            return goodsDao.getGoodsUserCategoryOrder(myId, userId, categoryId, orderId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsUserBrandOrder(String myId, String userId, int brandId, int orderId) throws BaseException {
        try {
            return goodsDao.getGoodsUserBrandOrder(myId, userId, brandId, orderId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsUserCategoryBrandOrder(String myId, String userId, int categoryId, int brandId, int orderId) throws BaseException {
        try {
            return goodsDao.getGoodsUserCategoryBrandOrder(myId, userId, categoryId, brandId, orderId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 찜 여부, 판매여부, 브랜드 리스트, 카테고리 리스트 추가해야함 -----------------------------------------------------

    public List<GetGoodsRes> getGoodsKeyword(String myId, String keyword) throws BaseException {
        try {
            return goodsDao.getGoodsKeyword(myId, keyword);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsKeywordCategory(String myId, String keyword, int categoryId) throws BaseException {
        try {
            return goodsDao.getGoodsKeywordCategory(myId, keyword, categoryId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsKeywordBrand(String myId, String keyword, int brandId) throws BaseException {
        try {
            return goodsDao.getGoodsKeywordBrand(myId, keyword, brandId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsKeywordOrder(String myId, String keyword, int orderId) throws BaseException {
        try {
            return goodsDao.getGoodsKeywordOrder(myId, keyword, orderId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsKeywordCategoryOrder(String myId, String keyword, int categoryId, int orderId) throws BaseException {
        try {
            return goodsDao.getGoodsKeywordCategoryOrder(myId, keyword, categoryId, orderId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsKeywordBrandOrder(String myId, String keyword, int brandId, int orderId) throws BaseException {
        try {
            return goodsDao.getGoodsKeywordBrandOrder(myId, keyword, brandId, orderId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsKeywordCategoryBrand(String myId, String keyword, int categoryId, int brandId) throws BaseException {
        try {
            return goodsDao.getGoodsKeywordCategoryBrand(myId, keyword, categoryId, brandId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetGoodsRes> getGoodsCategory(String myId, String categoryId) throws BaseException {
        try {
            return goodsDao.getGoodsCategory(myId, categoryId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetGoodsRes> getGoodsBrand(String myId, String brandId) throws BaseException {
        try {
            return goodsDao.getGoodsBrand(myId, brandId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetGoodsRes> getGoodsKeywordCategoryBrandOrder(String myId, String keyword, int categoryId, int brandId, int orderId) throws BaseException {
        try {
            return goodsDao.getGoodsKeywordCategoryBrandOrder(myId, keyword, categoryId, brandId, orderId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<Brand> getBrandUser(String userId) throws BaseException {
        try {
            return goodsDao.getBrandUser(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<Category> getCategoryUser(String userId) throws BaseException {
        try {
            return goodsDao.getCategoryUser(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<Brand> getBrandKeyword(String keyword) throws BaseException {
        try {
            return goodsDao.getBrandKeyword(keyword);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<Category> getCategoryKeyword(String keyword) throws BaseException {
        try {
            return goodsDao.getCategoryKeyword(keyword);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserStatusByUserId(String userId) throws BaseException {
        try {
            return goodsDao.checkUserStatusByUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<PictureGoodsInfo> getGoodsImgs(String goodsId) throws BaseException {
        try {
            return goodsDao.getGoodsImgsInfo(goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkGoodsId(String goodsId) throws BaseException {
        try {
            return goodsDao.checkGoodsId(goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkGoodsUserId(String userId) throws BaseException {
        try {
            return goodsDao.checkGoodsUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkGoodsUserId(String userId, String goodsId) throws BaseException {
        try {
            return goodsDao.checkGoodsUserId(userId, goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public Goods getGoodsDetail(String myId, String goodsId) throws BaseException {
        try {
            return goodsDao.getGoodsDetail(myId, goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetTag> getGoodsDetailTag(String goodsId) throws BaseException {
        try {
            return goodsDao.getGoodsDetailTag(goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getViews(String goodsId) throws BaseException {
        try {
            return goodsDao.getViews(goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
