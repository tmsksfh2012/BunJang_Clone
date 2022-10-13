package com.example.demo.src.goods;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.goods.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final GoodsProvider goodsProvider;
    @Autowired
    private final GoodsService goodsService;
    @Autowired
    private final JwtService jwtService;

    public GoodsController(GoodsProvider goodsProvider, GoodsService goodsService, JwtService jwtService){
        this.goodsProvider = goodsProvider;
        this.goodsService = goodsService;
        this.jwtService = jwtService;
    }

    /**
     * 추천 리스트 보기 API
     * [GET] /goods/home
     * @return BaseResponse<List<GetGoodsRes>>
     */
    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<List<GetGoodsRes>> getGoodsHomeList() {
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            List<GetGoodsRes> getGoodsRes = goodsProvider.getGoodsHomeList(userIdByJwt);

            if(getGoodsRes.isEmpty()) {
                return new BaseResponse<>(GET_GOODS_LIST_NO_EXIST);
            }

            return new BaseResponse<> (getGoodsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 브랜드 리스트 보기 API
     * [GET] /goods/brand-list
     * @return BaseResponse<List<BrandList>>
     */
    @ResponseBody
    @GetMapping("/brand-list")
    public BaseResponse<List<BrandList>> getGoodsBrandList() {
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<BrandList> getGoodsRes = goodsProvider.getGoodsBrandList(userIdByJwt);

            if(getGoodsRes.isEmpty()) {
                return new BaseResponse<>(NO_EXIST_BRAND_LIST);
            }

            return new BaseResponse<>(getGoodsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 카테고리 리스트 보기 API
     * [GET] /goods/category-list
     * @return BaseResponse<List<CategoryList>>
     */
    @ResponseBody
    @GetMapping("/category-list")
    public BaseResponse<List<CategoryList>> getGoodsCategoryList() {
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<CategoryList> getGoodsRes = goodsProvider.getGoodsCategoryList();

            if(getGoodsRes.isEmpty()) {
                return new BaseResponse<>(NO_EXIST_CATEGORY_LIST);
            }

            return new BaseResponse<>(getGoodsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 내 상품 리스트 보기 API
     * [GET] /goods
     * @return BaseResponse<List<GetGoodsRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetGoodsRes>> getGoodsMy() {
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if (goodsProvider.checkGoodsUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(GET_GOODS_LIST_NO_EXIST);
            }

            List<GetGoodsRes> getGoodsRes = goodsProvider.getGoodsMy(userIdByJwt);

            return new BaseResponse<>(getGoodsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 상품 리스트 보기 API
     * [GET] /goods/:userId
     * 상품 리스트 정렬 & 필터 보기 API
     * [GET] /goods/:userId? orderId= & brandId= & categoryId=
     * @return BaseResponse<List<GetGoodsRes>>
     */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetGoodsRes>> getGoodsUser(@PathVariable(required = false) String userId,
                                                            @RequestParam(required = false) String orderId,
                                                            @RequestParam(required = false) String brandId,
                                                            @RequestParam(required = false) String categoryId) {
        if (userId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        if (orderId != null && orderId.equals("")) {
            return new BaseResponse<>(NO_EXIST_ORDER_ID);
        }
        if (brandId != null && brandId.equals("")) {
            return new BaseResponse<>(NO_EXIST_BRAND_ID);
        }
        if (categoryId != null && categoryId.equals("")) {
            return new BaseResponse<>(NO_EXIST_CATEGORY_ID);
        }
        try {
            if(goodsProvider.checkUserStatusByUserId(userId) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            if(goodsProvider.checkUserStatusByUserId(userId) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }

            if (!isRegexInteger(userId)) {
                return new BaseResponse<>(INVAILD_PATH_VARIABLE);
            }
            if (goodsProvider.checkGoodsUserId(userId) == 0) {
                return new BaseResponse<>(GET_GOODS_LIST_NO_EXIST);
            }

            boolean o = true;
            boolean b = true;
            boolean c = true;
            //1 ~ 5까지
            if(orderId != null) o = Integer.parseInt(orderId) < 6 && Integer.parseInt(orderId) > 0;
            if(brandId != null) b = Integer.parseInt(brandId) < 11 && Integer.parseInt(brandId) > 0;
            if(categoryId != null) c = Integer.parseInt(categoryId) < 30 && Integer.parseInt(categoryId) > 0;

            //파라미터 예외 처리
            if(!(o && b && c)) return new BaseResponse<>(INVALID_QUERY_PARAMS);

            List<GetGoodsRes> getGoodsRes;
            //조건이 3개 -> 나올 수 있는 경우의 수 8가지
            if(orderId == null) {
                if(brandId == null){
                    if(categoryId == null) { //all is null - 전체 상품 조회
                        getGoodsRes = goodsProvider.getGoodsUser(userIdByJwt, userId);
                    }
                    else {
                        //orderId is null && brandId is null && categoryId is not null
                        //카테고리 조회
                        int category_id = Integer.parseInt(categoryId);
                        getGoodsRes = goodsProvider.getGoodsUserCategory(userIdByJwt, userId, category_id);
                    }
                }
                else { //orderId is null && brandId is not null
                    int brand_id = Integer.parseInt(brandId);
                    if(categoryId == null) { //categoryId is null - 브랜드 조회
                        getGoodsRes = goodsProvider.getGoodsUserBrand(userIdByJwt, userId, brand_id);
                    }
                    else { //categoryId is not null - 카테고리 && 브랜드 조회
                        int category_id = Integer.parseInt(categoryId);
                        getGoodsRes = goodsProvider.getGoodsUserCategoryBrand(userIdByJwt, userId, category_id, brand_id);
                    }
                }
            }
            else { //orderId is not null
                int order_id = Integer.parseInt(orderId);
                if(brandId == null){
                    if(categoryId == null) { //order is not null - 정렬 조회
                        getGoodsRes = goodsProvider.getGoodsUserOrder(userIdByJwt, userId, order_id);
                    }
                    else {
                        //orderId is not null && brandId is null && categoryId is not null
                        //카테고리 && 정렬 조회
                        int category_id = Integer.parseInt(categoryId);
                        getGoodsRes = goodsProvider.getGoodsUserCategoryOrder(userIdByJwt, userId, category_id, order_id);
                    }
                }
                else { //orderId is not null && brandId is not null
                    int brand_id = Integer.parseInt(brandId);
                    if(categoryId == null) { //categoryId is null - 브랜드 && 정렬 조회
                        getGoodsRes = goodsProvider.getGoodsUserBrandOrder(userIdByJwt, userId, brand_id, order_id);
                    }
                    else { //all is not null - 카테고리 && 브랜드 && 정렬 조회
                        int category_id = Integer.parseInt(categoryId);
                        getGoodsRes = goodsProvider.getGoodsUserCategoryBrandOrder(userIdByJwt, userId, category_id, brand_id, order_id);
                    }
                }
            }

            if(getGoodsRes.isEmpty()) {
                return new BaseResponse<>(GET_GOODS_LIST_NO_EXIST);
            }

            return new BaseResponse<>(getGoodsRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 유저 상품 리스트 필터 보기 API
     * [GET] /goods/:userId/filter
     * @return BaseResponse<GetFilterRes>>
     */
    @ResponseBody
    @GetMapping("/{userId}/filter")
    public BaseResponse<GetFiltersRes> getGoodsUserFilter(@PathVariable(required = false) String userId) {
        if (userId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        try {
            if(goodsProvider.checkUserStatusByUserId(userId) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            if(goodsProvider.checkUserStatusByUserId(userId) == 0) {
                return new BaseResponse<>(USERS_NOT_EXISTS);
            }

            if (!isRegexInteger(userId)) {
                return new BaseResponse<>(INVAILD_PATH_VARIABLE);
            }
            if (goodsProvider.checkGoodsUserId(userId) == 0) {
                return new BaseResponse<>(GET_GOODS_LIST_NO_EXIST);
            }
            try {
                List<Brand> brandList = goodsProvider.getBrandUser(userId);
                List<Category> categoryList = goodsProvider.getCategoryUser(userId);
                GetFiltersRes getFiltersRes = new GetFiltersRes(brandList, categoryList);
                return new BaseResponse<>(getFiltersRes);
            } catch (BaseException exception) {
                return new BaseResponse<>(GET_GOODS_FILTER_ERROR);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 검색어 상품 리스트 필터 보기 API
     * [GET] /goods/search/filter?keyword=
     * @return BaseResponse<GetFilterRes>>
     */
    @ResponseBody
    @GetMapping("/search/filter")
    public BaseResponse<GetFiltersRes> getGoodsSearchFilter(@RequestParam(required = false) String keyword) {
        if (keyword == null) {
            return new BaseResponse<>(EMPTY_QUERY_PARAMS);
        }
        if (keyword.equals("")) {
            return new BaseResponse<>(NO_EXIST_KEYWORD);
        }
        //------키워드 validation ??
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            try {
                List<Brand> brandList = goodsProvider.getBrandKeyword(keyword);
                List<Category> categoryList = goodsProvider.getCategoryKeyword(keyword);
                GetFiltersRes getFiltersRes = new GetFiltersRes(brandList, categoryList);
                return new BaseResponse<>(getFiltersRes);
            } catch (BaseException exception) {
                return new BaseResponse<>(GET_GOODS_FILTER_ERROR);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 상세 보기 API
     * [GET] /goods/detail/:goodsId
     * @return BaseResponse<Goods>
     */
    @ResponseBody
    @GetMapping("/detail/{goodsId}")
    public BaseResponse<Goods> getGoodsDetail(@PathVariable(required = false) String goodsId) {
        if (goodsId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if (!isRegexInteger(goodsId)) {
                return new BaseResponse<>(INVAILD_PATH_VARIABLE);
            }
            if (goodsProvider.checkGoodsId(goodsId) == 0) {
                return new BaseResponse<>(GET_GOODS_NOT_EXIST);
            }

            goodsService.postViews(userIdByJwt, goodsId);

            Goods getGoodsDetail = goodsProvider.getGoodsDetail(userIdByJwt, goodsId);
            getGoodsDetail.setGoodsImgList(goodsProvider.getGoodsImgs(goodsId));
            getGoodsDetail.setViews(goodsProvider.getViews(goodsId));
            getGoodsDetail.setTagList(goodsProvider.getGoodsDetailTag(goodsId));


            return new BaseResponse<>(getGoodsDetail);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 검색어 상품 리스트 보기 API
     * [GET] /goods/search? keyword=
     * 상품 리스트 정렬 & 필터 보기 API
     * [GET] /goods/search? keyword= & orderId= & brandId= & categoryId=
     * @return BaseResponse<List<GetGoodsRes>>
     */
    @ResponseBody
    @GetMapping("/search")
    public BaseResponse<List<GetGoodsRes>> getGoodsSearch(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false) String orderId,
                                                      @RequestParam(required = false) String brandId,
                                                      @RequestParam(required = false) String categoryId) {
        if (keyword == null) {
            return new BaseResponse<>(EMPTY_QUERY_PARAMS);
        }
        if (keyword.equals("")) {
            return new BaseResponse<>(NO_EXIST_KEYWORD);
        }
        if (orderId != null && orderId.equals("")) {
            return new BaseResponse<>(NO_EXIST_ORDER_ID);
        }
        if (brandId != null && brandId.equals("")) {
            return new BaseResponse<>(NO_EXIST_BRAND_ID);
        }
        if (categoryId != null && categoryId.equals("")) {
            return new BaseResponse<>(NO_EXIST_CATEGORY_ID);
        }

        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            boolean o = true;
            boolean b = true;
            boolean c = true;
            //1 ~ 5까지
            if(orderId != null) o = Integer.parseInt(orderId) < 6 && Integer.parseInt(orderId) > 0;
            if(brandId != null) b = Integer.parseInt(brandId) < 11 && Integer.parseInt(brandId) > 0;
            if(categoryId != null) c = Integer.parseInt(categoryId) < 30 && Integer.parseInt(categoryId) > 0;

            //파라미터 예외 처리
            if(!(o && b && c)) return new BaseResponse<>(INVALID_QUERY_PARAMS);

            List<GetGoodsRes> getGoodsKeywordRes;
            //조건이 3개 -> 나올 수 있는 경우의 수 8가지
            if(orderId == null) {
                if(brandId == null){
                    if(categoryId == null) { //all is null - 전체 상품 조회
                        getGoodsKeywordRes = goodsProvider.getGoodsKeyword(userIdByJwt, keyword);
                    }
                    else {
                        //orderId is null && brandId is null && categoryId is not null
                        //카테고리 조회
                        int category_id = Integer.parseInt(categoryId);
                        getGoodsKeywordRes = goodsProvider.getGoodsKeywordCategory(userIdByJwt, keyword, category_id);
                    }
                }
                else { //orderId is null && brandId is not null
                    int brand_id = Integer.parseInt(brandId);
                    if(categoryId == null) { //categoryId is null - 브랜드 조회
                        getGoodsKeywordRes = goodsProvider.getGoodsKeywordBrand(userIdByJwt, keyword, brand_id);
                    }
                    else { //categoryId is not null - 카테고리 && 브랜드 조회
                        int category_id = Integer.parseInt(categoryId);
                        getGoodsKeywordRes = goodsProvider.getGoodsKeywordCategoryBrand(userIdByJwt, keyword, category_id, brand_id);
                    }
                }
            }
            else { //orderId is not null
                int order_id = Integer.parseInt(orderId);
                if(brandId == null){
                    if(categoryId == null) { //order is not null - 정렬 조회
                        getGoodsKeywordRes = goodsProvider.getGoodsKeywordOrder(userIdByJwt, keyword, order_id);
                    }
                    else {
                        //orderId is not null && brandId is null && categoryId is not null
                        //카테고리 && 정렬 조회
                        int category_id = Integer.parseInt(categoryId);
                        getGoodsKeywordRes = goodsProvider.getGoodsKeywordCategoryOrder(userIdByJwt, keyword, category_id, order_id);
                    }
                }
                else { //orderId is not null && brandId is not null
                    int brand_id = Integer.parseInt(brandId);
                    if(categoryId == null) { //categoryId is null - 브랜드 && 정렬 조회
                        getGoodsKeywordRes = goodsProvider.getGoodsKeywordBrandOrder(userIdByJwt, keyword, brand_id, order_id);
                    }
                    else { //all is not null - 카테고리 && 브랜드 && 정렬 조회
                        int category_id = Integer.parseInt(categoryId);
                        getGoodsKeywordRes = goodsProvider.getGoodsKeywordCategoryBrandOrder(userIdByJwt, keyword, category_id, brand_id, order_id);
                    }
                }
            }

            if(getGoodsKeywordRes.isEmpty()) {
                return new BaseResponse<>(GET_GOODS_LIST_NO_EXIST);
            }

            goodsService.postKeyword(keyword, userIdByJwt);
            return new BaseResponse<>(getGoodsKeywordRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 카테고리 상품 리스트 보기 API
     * [GET] /goods/category/:categoryId
     * @return BaseResponse<List<GetGoodsRes>>
     */
    @ResponseBody
    @GetMapping("/category/{categoryId}")
    public BaseResponse<List<GetGoodsRes>> getGoodsCategory(@PathVariable(required = false) String categoryId ) {
        if (categoryId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }

        if(Integer.parseInt(categoryId) > 29 && Integer.parseInt(categoryId) < 1) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }

        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetGoodsRes> getGoodsCategoryRes = goodsProvider.getGoodsCategory(userIdByJwt, categoryId);

            if(getGoodsCategoryRes.isEmpty()) {
                return new BaseResponse<>(GET_GOODS_LIST_NO_EXIST);
            }

            return new BaseResponse<>(getGoodsCategoryRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 브랜드 상품 리스트 보기 API
     * [GET] /goods/brand/:brandId
     * @return BaseResponse<List<GetGoodsRes>>
     */
    @ResponseBody
    @GetMapping("/brand/{brandId}")
    public BaseResponse<List<GetGoodsRes>> getGoodsBrand(@PathVariable(required = false) String brandId ) {
        if (brandId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }

        if(!isRegexInteger(brandId)) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }

        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetGoodsRes> getGoodsBrandRes = goodsProvider.getGoodsBrand(userIdByJwt, brandId);

            if(getGoodsBrandRes.isEmpty()) {
                return new BaseResponse<>(GET_GOODS_LIST_NO_EXIST);
            }

            return new BaseResponse<>(getGoodsBrandRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 상품 작성 API
     * [POST] /goods
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postGoods(@RequestBody PostGoodsReq postGoodsReq) {
        if(postGoodsReq == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        if(postGoodsReq.getTitle() == null){
            return new BaseResponse<>(POST_GOODS_EMPTY_TITLE);
        }
        if(postGoodsReq.getContent() == null || postGoodsReq.getContent().length() < 10){
            return new BaseResponse<>(POST_GOODS_EMPTY_CONTENT);
        }
        if(postGoodsReq.getPrice() == null || postGoodsReq.getPrice() < 100){
            return new BaseResponse<>(POST_GOODS_INVALID_PRICE);
        }
        if(postGoodsReq.getTagList().isEmpty()){
            return new BaseResponse<>(POST_GOODS_EMPTY_TAG);
        }
        if(postGoodsReq.getGoodsImgList().isEmpty()) {
            return new BaseResponse<>(POST_GOODS_EMPTY_IMGS);
        }
        if(postGoodsReq.getCategoryId() == null) {
            return new BaseResponse<>(POST_GOODS_EMPTY_CATEGORY);
        }
        if(postGoodsReq.getNumberOfGoods() == null) {
            postGoodsReq.setNumberOfGoods(1);
        }
        if(postGoodsReq.getIsFreeShipping() == null) {
            postGoodsReq.setIsFreeShipping("n");
        }
        if(postGoodsReq.getIsUserGoods() == null) {
            postGoodsReq.setIsUserGoods("n");
        }
        if(postGoodsReq.getIsAdvertising() == null) {
            postGoodsReq.setIsAdvertising("n");
        }
        if(postGoodsReq.getCanBungaePay() == null) {
            postGoodsReq.setCanBungaePay("y");
        }
        if(postGoodsReq.getCanExchange() == null) {
            postGoodsReq.setCanExchange("n");
        }

        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (goodsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            int goodsId = goodsService.postGoods(userIdByJwt, postGoodsReq);
            String goods_id = String.valueOf(goodsId);

            goodsService.postWishList(goods_id);

            List<PictureGoods> goodsImgList = postGoodsReq.getGoodsImgList();
            for(int i = 0; i < goodsImgList.size(); i++) {
                String isFirst = i == 0 ? "y" : "n'";
                goodsService.postGoodsImg(goods_id, goodsImgList.get(i), isFirst);
            }

            List<Tag> tagList = postGoodsReq.getTagList();
            for(Tag tag : tagList){
                goodsService.postGoodsTag(goods_id, tag);
            }

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 물건 판매 글 수정 API
     * [PATCH] /goods/:goodsId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{goodsId}")
    public BaseResponse<String> modifyGoods(@PathVariable("goodsId") String goodsId, @RequestBody PatchGoodsReq patchGoodsReq){
        if (goodsId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        if(patchGoodsReq == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        if(patchGoodsReq.getTitle() == null){
            return new BaseResponse<>(POST_GOODS_EMPTY_TITLE);
        }
        if(patchGoodsReq.getContent() == null ||patchGoodsReq.getContent().length() < 10){
            return new BaseResponse<>(POST_GOODS_EMPTY_CONTENT);
        }
        if(patchGoodsReq.getPrice() == null || patchGoodsReq.getPrice() < 100){
            return new BaseResponse<>(POST_GOODS_INVALID_PRICE);
        }
        if(patchGoodsReq.getTagList().isEmpty()){
            return new BaseResponse<>(POST_GOODS_EMPTY_TAG);
        }
        if(patchGoodsReq.getGoodsImgList().isEmpty()) {
            return new BaseResponse<>(POST_GOODS_EMPTY_IMGS);
        }
        if(patchGoodsReq.getCategoryId() == null) {
            return new BaseResponse<>(POST_GOODS_EMPTY_CATEGORY);
        }
        if(patchGoodsReq.getNumberOfGoods() == null) {
            patchGoodsReq.setNumberOfGoods(1);
        }
        if(patchGoodsReq.getIsFreeShipping() == null) {
            patchGoodsReq.setIsFreeShipping("n");
        }
        if(patchGoodsReq.getIsUserGoods() == null) {
            patchGoodsReq.setIsUserGoods("n");
        }
        if(patchGoodsReq.getIsAdvertising() == null) {
            patchGoodsReq.setIsAdvertising("n");
        }
        if(patchGoodsReq.getCanBungaePay() == null) {
            patchGoodsReq.setCanBungaePay("y");
        }
        if(patchGoodsReq.getCanExchange() == null) {
            patchGoodsReq.setCanExchange("n");
        }
        if (!isRegexInteger(goodsId)) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }
        try {
            //jwt에서 idx 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());

            if(goodsProvider.checkGoodsUserId(userIdByJwt, goodsId) == 0) {
                return new BaseResponse<>(GET_GOODS_NOT_EXIST);
            }

            goodsService.patchGoods(goodsId, patchGoodsReq);
            //기존 태그 및 사진 삭제
            goodsService.patchGoodsTag(goodsId);
            goodsService.patchGoodsImg(goodsId);

            List<PictureGoods> goodsImgList = patchGoodsReq.getGoodsImgList();
            for(int i = 0; i < goodsImgList.size(); i++) {
                String isFirst = i == 0 ? "y" : "n'";
                goodsService.postGoodsImg(goodsId, goodsImgList.get(i), isFirst);
            }


            List<Tag> tagList = patchGoodsReq.getTagList();
            for(Tag tag : tagList){
                goodsService.postGoodsTag(goodsId, tag);
            }

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 물건 판매 글 삭제 API
     * [PATCH] /goods/removal:goodsId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/removal/{goodsId}")
    public BaseResponse<String> deleteGoods(@PathVariable("goodsId") String goodsId){
        if (goodsId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        if (!isRegexInteger(goodsId)) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }
        try {
            //jwt에서 idx 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());

            if(goodsProvider.checkGoodsUserId(userIdByJwt, goodsId) == 0) {
                return new BaseResponse<>(GET_GOODS_NOT_EXIST);
            }

            goodsService.deleteGoods(goodsId);
            //기존 태그 및 사진 삭제
            goodsService.patchGoodsTag(goodsId);
            goodsService.patchGoodsImg(goodsId);

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}