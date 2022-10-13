package com.example.demo.src.goods;

import com.example.demo.src.goods.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class GoodsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetGoodsRes> getGoodsHomeList(String myId) {
        String getGoodsHomeQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                "from Goods g " +
                "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                "left outer join WishList w on g.goodsId = w.goodsId " +
                "left outer join Wish ws on w.wlId = ws.wlId" +
                " where g.status != 'delete' and isFirst = 'Y' and w.status = 'normal'\n" +
                "group by g.goodsId Order by rand() Limit 100";
        String getGoodsHomeParams = myId;

        return this.jdbcTemplate.query(getGoodsHomeQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsHomeParams);
    }

    public List<BrandList> getGoodsBrandList(String userId) {
        String getGoodsBrandList = "select b.brandId, b.brandName, b.brandEngName, b.imgUrl,\n" +
                "(select count(*) from Goods g where g.brandId = b.brandId and g.status != 'delete') as counting,\n" +
                "if(exists(select * from BrandFollowerList bfl where bfl.userId = ? and bfl.brandId = b.brandId),'Y', 'N') as isLike\n" +
                "from Brand b, User u, Goods g group by brandId";
        String getGoodsBrandListParams = userId;

        return this.jdbcTemplate.query(getGoodsBrandList,
                (rs, rowNum) -> new BrandList(
                        rs.getInt("brandId"),
                        rs.getString("brandName"),
                        rs.getString("brandEngName"),
                        rs.getString("imgUrl"),
                        rs.getInt("counting"),
                        rs.getString("isLike")),
                getGoodsBrandListParams);
    }
    public List<CategoryList> getGoodsCategoryList() {
        String getGoodsBrandList = "select c.categoryId, c.name as categoryName, c.imgUrl as imgUrl  from Category c where c.status = 'normal'";

        return this.jdbcTemplate.query(getGoodsBrandList,
                (rs, rowNum) -> new CategoryList(
                        rs.getInt("categoryId"),
                        rs.getString("categoryName"),
                        rs.getString("imgUrl")));
    }

    public List<GetGoodsRes> getGoodsMy(String myId) {
        String getGoodsMyQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                "g.canBungaePay as canBungaePay, p.imgUrl, g.status as status " +
                "from Goods g " +
                "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                "left outer join WishList w on g.goodsId = w.goodsId " +
                "left outer join Wish ws on w.wlId = ws.wlId \n" +
                "where g.status != 'delete' and w.status = 'normal' and g.sellerId =? and isFirst = 'Y'\n" +
                "group by g.goodsId";
        String getGoodsMyParams = myId;

        return this.jdbcTemplate.query(getGoodsMyQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        null,
                        rs.getString("status")),
                getGoodsMyParams);
    }
    //유저 상품 리스트 조회
    public List<GetGoodsRes> getGoodsUser(String myId, String userId) {
        String getGoodsUserQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                "from Goods g " +
                "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                "left outer join WishList w on g.goodsId = w.goodsId " +
                "left outer join Wish ws on w.wlId = ws.wlId " +
                "where g.sellerId = ? and w.status = 'normal' and g.status != 'delete' and isFirst = 'Y'\n" +
                "group by g.goodsId";
        Object[] getGoodsUserParams = new Object[]{myId, userId};

        return this.jdbcTemplate.query(getGoodsUserQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsUserParams);
    }
    //유저 상품 리스트 카테고리 조회
    public List<GetGoodsRes> getGoodsUserCategory(String myId, String userId, int categoryId) {
        String getGoodsUserCategoryQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                " g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                " from Goods g " +
                "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                "left outer join WishList w on g.goodsId = w.goodsId " +
                "left outer join Wish ws on w.wlId = ws.wlId where w.status = 'normal' and g.status != 'delete'  and sellerId = ? and categoryId = ? and isFirst = 'Y'\n" +
                "group by goodsId";
        Object[] getGoodsUserCategoryParams = new Object[]{myId, userId, categoryId};

        return this.jdbcTemplate.query(getGoodsUserCategoryQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),

                getGoodsUserCategoryParams);
    }
    //유저 상품 리스트 브랜드 조회
    public List<GetGoodsRes> getGoodsUserBrand(String myId, String userId, int brandId) {
        String getGoodsUserBrandQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                " g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                " from Goods g " +
                "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                "left outer join WishList w on g.goodsId = w.goodsId " +
                "left outer join Wish ws on w.wlId = ws.wlId" +
                " where g.status != 'delete' and w.status = 'normal' and sellerId = ? and brandId = ? and isFirst = 'Y'\n" +
                "group by goodsId";
        Object[] getGoodsUserBrandParams = new Object[]{myId, userId, brandId};

        return this.jdbcTemplate.query(getGoodsUserBrandQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsUserBrandParams);
    }
    //유저 상품 리스트 카테고리 && 브랜드 조회
    public List<GetGoodsRes> getGoodsUserCategoryBrand(String myId, String userId, int categoryId, int brandId) {
        String getGoodsUserCategoryBrandQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                " g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                " from Goods g " +
                "inner join PicturesGoods p on g.goodsId = p.goodsId" +
                " left outer join WishList w on g.goodsId = w.goodsId " +
                "left outer join Wish ws on w.wlId = ws.wlId " +
                "where g.status != 'delete' and w.status = 'normal' and sellerId = ? and categoryId = ? and brandId = ? and isFirst = 'Y'\n" +
                "group by goodsId";
        Object[] getGoodsUserCategoryBrandParams = new Object[]{myId, userId, categoryId, brandId};

        return this.jdbcTemplate.query(getGoodsUserCategoryBrandQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsUserCategoryBrandParams);
    }
    //유저 상품 리스트 정렬 조회
    public List<GetGoodsRes> getGoodsUserOrder(String myId, String userId, int orderId) {
        //orderId -> 1 - 최신순, 2 - 인기순(찜순), 3 - 낮은가격, 4 - 높은가격, 5 - 거리순
        String getGoodsUserOrderQuery = "";
        switch(orderId) {
            case 1: default:
                getGoodsUserOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g " +
                        "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                        "left outer join WishList w on g.goodsId = w.goodsId " +
                        "left outer join Wish ws on w.wlId = ws.wlId\n" +
                        "where g.status != 'delete' and w.status = 'normal' and sellerId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, g.updatedAt order by g.updatedAt asc";
                break;
            case 2:
                getGoodsUserOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g " +
                        "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                        "left outer join WishList w on g.goodsId = w.goodsId " +
                        "left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and sellerId = ? and isFirst = 'Y'\n" +
                        "group by goodsId order by count(if(ws.wishId, ws.wishId, null)) desc";
                break;
            case 3:
                getGoodsUserOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g " +
                        "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                        "left outer join WishList w on g.goodsId = w.goodsId " +
                        "left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and sellerId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price asc";
                break;
            case 4:
                getGoodsUserOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g " +
                        "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                        "left outer join WishList w on g.goodsId = w.goodsId " +
                        "left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and sellerId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price desc";
                break;
            case 5:
                break;
        }

        Object[] getGoodsUserOrderParams = new Object[]{myId, userId};

        return this.jdbcTemplate.query(getGoodsUserOrderQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsUserOrderParams);
    }
    //유저 상품 리스트 카테고리 && 정렬 조회
    public List<GetGoodsRes> getGoodsUserCategoryOrder(String myId, String userId, int categoryId, int orderId) {
        //orderId -> 1 - 최신순, 2 - 인기순(찜순), 3 - 낮은가격, 4 - 높은가격, 5 - 거리순
        String getGoodsUserCategoryOrderQuery = "";
        switch (orderId) {
            case 1:
            default:
                getGoodsUserCategoryOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId\n" +
                        "where g.status != 'delete' and w.status = 'normal' and sellerId = ? and categoryId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, g.updatedAt order by g.updatedAt asc";
                break;
            case 2:
                getGoodsUserCategoryOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and sellerId = ? and categoryId = ? and isFirst = 'Y'\n" +
                        "group by goodsId order by count(if(ws.wishId, ws.wishId, null)) desc";
                break;
            case 3:
                getGoodsUserCategoryOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and sellerId = ? and categoryId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price asc";
                break;
            case 4:
                getGoodsUserCategoryOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and sellerId = ? and categoryId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price desc";
                break;
            case 5:
                break;
        }
        Object[] getGoodsUserCategoryOrderParams = new Object[]{myId, userId, categoryId};

        return this.jdbcTemplate.query(getGoodsUserCategoryOrderQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsUserCategoryOrderParams);
    }
    //유저 상품 리스트 브랜드 && 정렬 조회
    public List<GetGoodsRes> getGoodsUserBrandOrder(String myId, String userId, int brandId, int orderId) {
        //orderId -> 1 - 최신순, 2 - 인기순(찜순), 3 - 낮은가격, 4 - 높은가격, 5 - 거리순
        String getGoodsUserBrandOrderQuery = "";
        switch (orderId) {
            case 1:
            default:
                getGoodsUserBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId\n" +
                        "where g.status != 'delete' and w.status = 'normal'  and sellerId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, g.updatedAt order by g.updatedAt asc";
                break;
            case 2:
                getGoodsUserBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId where g.status != 'delete' and w.status = 'normal' and sellerId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId order by count(if(ws.wishId, ws.wishId, null)) desc";
                break;
            case 3:
                getGoodsUserBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId where g.status != 'delete' and w.status = 'normal' and sellerId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price asc";
                break;
            case 4:
                getGoodsUserBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId where g.status != 'delete' and w.status = 'normal' and sellerId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price desc";
                break;
            case 5:
                break;
        }
        Object[] getGoodsUserBrandOrderParams = new Object[]{myId, userId, brandId};

        return this.jdbcTemplate.query(getGoodsUserBrandOrderQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsUserBrandOrderParams);
    }
    //유저 상품 리스트 카테고리 && 브랜드 && 정렬 조회
    public List<GetGoodsRes> getGoodsUserCategoryBrandOrder(String myId, String userId, int categoryId, int brandId, int orderId) {
        //orderId -> 1 - 최신순, 2 - 인기순(찜순), 3 - 낮은가격, 4 - 높은가격, 5 - 거리순
        String getGoodsUserCategoryBrandOrderQuery = "";
        switch (orderId) {
            case 1:
            default:
                getGoodsUserCategoryBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId\n" +
                        "where g.status != 'delete' and w.status = 'normal'  and sellerId = ? and categoryId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, g.updatedAt order by g.updatedAt asc";
                break;
            case 2:
                getGoodsUserCategoryBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId where g.status != 'delete' and w.status = 'normal' and sellerId = ? and categoryId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId order by count(if(ws.wishId, ws.wishId, null)) desc";
                break;
            case 3:
                getGoodsUserCategoryBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId where g.status != 'delete' and w.status = 'normal' and sellerId = ? and categoryId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price asc";
                break;
            case 4:
                getGoodsUserCategoryBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId where g.status != 'delete' and w.status = 'normal' and sellerId = ? and categoryId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price desc";
                break;
            case 5:
                break;
        }
        Object[] getGoodsUserCategoryBrandOrderParams = new Object[]{myId, userId, categoryId, brandId};

        return this.jdbcTemplate.query(getGoodsUserCategoryBrandOrderQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsUserCategoryBrandOrderParams);
    }
    //키워드 상품 조회
    public List<GetGoodsRes> getGoodsKeyword(String myId, String keyword) {
        String getGoodsKeywordQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                "                 g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                "                 from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId where g.status != 'delete'" +
                " and w.status = 'normal' and isFirst = 'Y' and (g.title like ? or g.content like ?)\n" +
                "                group by goodsId";
        String _keyword = "%" + keyword + "%";
        Object[] getGoodsKeywordParams = new Object[]{myId, _keyword, _keyword};

        return this.jdbcTemplate.query(getGoodsKeywordQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsKeywordParams);
    }
    //키워드 상품 리스트 카테고리 조회
    public List<GetGoodsRes> getGoodsKeywordCategory(String myId, String keyword, int categoryId) {
        String getGoodsKeywordCategoryQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes, \n" +
                "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId where g.status != 'delete' and w.status = 'normal' " +
                "and (g.title like ? or g.content like ?) and g.categoryId = ? and isFirst = 'Y'\n" +
                "group by goodsId";
        String _keyword = "%" + keyword + "%";
        Object[] getGoodsKeywordCategoryParams = new Object[]{myId, _keyword, _keyword, categoryId};

        return this.jdbcTemplate.query(getGoodsKeywordCategoryQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsKeywordCategoryParams);
    }
    //키워드 상품 리스트 브랜드 조회
    public List<GetGoodsRes> getGoodsKeywordBrand(String myId, String keyword, int brandId) {
        String getGoodsUserBrandQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes, \n" +
                "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId" +
                " where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?)  and g.brandId = ? and isFirst = 'Y'\n" +
                "group by goodsId";
        String _keyword = "%" + keyword + "%";
        Object[] getGoodsKeywordBrandParams = new Object[]{myId, _keyword, _keyword, brandId};

        return this.jdbcTemplate.query(getGoodsUserBrandQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsKeywordBrandParams);
    }
    //키워드 상품 리스트 카테고리 && 브랜드 조회
    public List<GetGoodsRes> getGoodsKeywordCategoryBrand(String myId, String keyword, int categoryId, int brandId) {
        String getGoodsKeywordCategoryBrandQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes," +
                " g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                " from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?)  and categoryId = ? and brandId = ? and isFirst = 'Y'" +
                "group by goodsId";
        String _keyword = "%" + keyword + "%";
        Object[] getGoodsKeywordCategoryBrandParams = new Object[]{myId, _keyword, _keyword, categoryId, brandId};

        return this.jdbcTemplate.query(getGoodsKeywordCategoryBrandQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsKeywordCategoryBrandParams);
    }
    //키워드 상품 리스트 정렬 조회
    public List<GetGoodsRes> getGoodsKeywordOrder(String myId, String keyword, int orderId) {
        //orderId -> 1 - 최신순, 2 - 인기순(찜순), 3 - 낮은가격, 4 - 높은가격, 5 - 거리순
        String getGoodsKeywordOrderQuery = "";
        switch(orderId) {
            case 1: default:
                getGoodsKeywordOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and isFirst = 'Y'\n" +
                        "group by goodsId, g.updatedAt order by g.updatedAt asc";
                break;
            case 2:
                getGoodsKeywordOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId" +
                        " where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and isFirst = 'Y'\n" +
                        "group by goodsId order by count(if(ws.wishId, ws.wishId, null)) desc";
                break;
            case 3:
                getGoodsKeywordOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId" +
                        " where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price asc";
                break;
            case 4:
                getGoodsKeywordOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price desc";
                break;
            case 5:
                break;
        }

        String _keyword = "%" + keyword + "%";
        Object[] getGoodsKeywordOrderParams = new Object[]{myId, _keyword, _keyword};


        return this.jdbcTemplate.query(getGoodsKeywordOrderQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsKeywordOrderParams);
    }
    //키워드 상품 리스트 카테고리 && 정렬 조회
    public List<GetGoodsRes> getGoodsKeywordCategoryOrder(String myId, String keyword, int categoryId, int orderId) {
        //orderId -> 1 - 최신순, 2 - 인기순(찜순), 3 - 낮은가격, 4 - 높은가격, 5 - 거리순
        String getGoodsKeywordCategoryOrderQuery = "";
        switch (orderId) {
            case 1:
            default:
                getGoodsKeywordCategoryOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and categoryId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, g.updatedAt order by g.updatedAt asc";
                break;
            case 2:
                getGoodsKeywordCategoryOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and categoryId = ? and isFirst = 'Y'\n" +
                        "group by goodsId order by count(if(ws.wishId, ws.wishId, null)) desc";
                break;
            case 3:
                getGoodsKeywordCategoryOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and categoryId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price asc";
                break;
            case 4:
                getGoodsKeywordCategoryOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and categoryId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price desc";
                break;
            case 5:
                break;
        }

        String _keyword = "%" + keyword + "%";
        Object[] getGoodsKeywordCategoryOrderParams = new Object[]{myId, _keyword, _keyword, categoryId};

        return this.jdbcTemplate.query(getGoodsKeywordCategoryOrderQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsKeywordCategoryOrderParams);
    }
    //키워드 상품 리스트 브랜드 && 정렬 조회
    public List<GetGoodsRes> getGoodsKeywordBrandOrder(String myId, String keyword, int brandId, int orderId) {
        //orderId -> 1 - 최신순, 2 - 인기순(찜순), 3 - 낮은가격, 4 - 높은가격, 5 - 거리순
        String getGoodsKeywordBrandOrderQuery = "";
        switch (orderId) {
            case 1:
            default:
                getGoodsKeywordBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, g.updatedAt order by g.updatedAt asc";
                break;
            case 2:
                getGoodsKeywordBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId order by count(if(ws.wishId, ws.wishId, null)) desc";
                break;
            case 3:
                getGoodsKeywordBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price asc";
                break;
            case 4:
                getGoodsKeywordBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price desc";
                break;
            case 5:
                break;
        }
        String _keyword = "%" + keyword + "%";
        Object[] getGoodsKeywordBrandOrderParams = new Object[]{myId, _keyword, _keyword, brandId};

        return this.jdbcTemplate.query(getGoodsKeywordBrandOrderQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsKeywordBrandOrderParams);
    }
    //키워드 상품 리스트 카테고리 && 브랜드 && 정렬 조회
    public List<GetGoodsRes> getGoodsKeywordCategoryBrandOrder(String myId, String keyword, int categoryId, int brandId, int orderId) {
        //orderId -> 1 - 최신순, 2 - 인기순(찜순), 3 - 낮은가격, 4 - 높은가격, 5 - 거리순
        String getGoodsKeywordCategoryBrandOrderQuery = "";
        switch (orderId) {
            case 1:
            default:
                getGoodsKeywordCategoryBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and categoryId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, g.updatedAt order by g.updatedAt asc";
                break;
            case 2:
                getGoodsKeywordCategoryBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and categoryId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId order by count(if(ws.wishId, ws.wishId, null)) desc";
                break;
            case 3:
                getGoodsKeywordCategoryBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and categoryId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price asc";
                break;
            case 4:
                getGoodsKeywordCategoryBrandOrderQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes,\n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId left outer join WishList w on g.goodsId = w.goodsId left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and (g.title like ? or g.content like ?) and categoryId = ? and brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId, price order by g.price desc";
                break;
            case 5:
                break;
        }
        String _keyword = "%" + keyword + "%";
        Object[] getGoodsKeywordCategoryBrandOrderParams = new Object[]{myId, _keyword, _keyword, categoryId, brandId};

        return this.jdbcTemplate.query(getGoodsKeywordCategoryBrandOrderQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),rs.getString("status")),
                getGoodsKeywordCategoryBrandOrderParams);
    }

    //카테고리 상품 리스트 조회
    public List<GetGoodsRes> getGoodsCategory(String myId, String categoryId) {
        String getGoodsCategoryQuery =
                "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes, \n" +
                "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                "from Goods g " +
                "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                "left outer join WishList w on g.goodsId = w.goodsId " +
                "left outer join Wish ws on w.wlId = ws.wlId " +
                "where g.status != 'delete' and w.status = 'normal'" +
                "and g.categoryId = ? and isFirst = 'Y'\n" +
                "group by goodsId";
        Object[] getGoodsCategoryParams = new Object[] {myId, categoryId};
        return this.jdbcTemplate.query(getGoodsCategoryQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),
                        rs.getString("status")),
                getGoodsCategoryParams);
    }

    //카테고리 상품 리스트 조회
    public List<GetGoodsRes> getGoodsBrand(String myId, String brandId) {
        String getGoodsBrandQuery =
                "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price, if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes, \n" +
                        "g.canBungaePay as canBungaePay, p.imgUrl, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike, g.status as status\n" +
                        "from Goods g " +
                        "inner join PicturesGoods p on g.goodsId = p.goodsId " +
                        "left outer join WishList w on g.goodsId = w.goodsId " +
                        "left outer join Wish ws on w.wlId = ws.wlId " +
                        "where g.status != 'delete' and w.status = 'normal' and g.brandId = ? and isFirst = 'Y'\n" +
                        "group by goodsId";
        Object[] getGoodsBrandParams = new Object[] {myId, brandId};
        return this.jdbcTemplate.query(getGoodsBrandQuery,
                (rs, rowNum) -> new GetGoodsRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("likes"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("isLike"),
                        rs.getString("status")),
                getGoodsBrandParams);
    }

    //상품 상세 조회
    public Goods getGoodsDetail(String myId, String goodsId) {
        String getGoodsDetailQuery = "select g.goodsId as goodsId, g.title as title, g.content as content,\n" +
                "u.nickName as seller, CONCAT(FORMAT(g.price, 0) , '원') as price, g.categoryId as categoryId, g.isFreeShipping as isFreeShipping,\n" +
                "g.numberOfGoods as numberOfGoods, g.isUserGoods as isUserGoods, g.canExchange as canExchange,\n" +
                "g.isAdvertising as isAdvertising, g.canBungaePay as canBungaePay,\n" +
                "if(g.regionId is null, '', r.regionName) as region, g.status as status,\n" +
                "if(ws.wishId is not null and ws.status = 'normal', count(ws.wishId),0) as likes, if(ws.wishId is not null and ws.status = 'normal' and ws.userId = ?, 'Y', 'N') as isLike\n" +
                "from Goods g\n" +
                "inner join User u on g.sellerId = u.userId\n" +
                "left outer join RegionList r on g.regionId = r.regionId\n" +
                "left outer join WishList w on g.goodsId = w.goodsId\n" +
                "left outer join Wish ws on w.wlId = ws.wlId\n" +
                "where g.goodsId = ? and g.status != 'delete' and w.status = 'normal'";
        Object[] getGoodsDetailParams = new Object[] {myId, goodsId};

        return this.jdbcTemplate.queryForObject(getGoodsDetailQuery,
                (rs, rowNum) -> new Goods(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("seller"),
                        rs.getString("price"),
                        rs.getInt("categoryId"),
                        rs.getString("isFreeShipping"),
                        rs.getInt("numberOfGoods"),
                        rs.getString("isUserGoods"),
                        rs.getString("canExchange"),
                        rs.getString("isAdvertising"),
                        rs.getString("canBungaePay"),
                        rs.getString("region"),
                        rs.getString("status"),
                        rs.getInt("likes"),
                        rs.getString("isLike")),
                getGoodsDetailParams);
    }

    public List<GetTag> getGoodsDetailTag(String goodsId) {
        String getGoodsDetailTagQuery =
                "select tgId as tagId, content from TagsGoods where goodsId = ?";
        return this.jdbcTemplate.query(getGoodsDetailTagQuery,
                (rs, rowNum) -> new GetTag(
                        rs.getInt("tagId"),
                        rs.getString("content")),
                goodsId);
    }

    public void postViews(String myId, String goodsId) {
        String postViewsQuery =
                "insert into ViewsGoods(goodsId, userId) values(?, ?)";
        Object[] postViewsParams = new Object[] {goodsId, myId};
        this.jdbcTemplate.update(postViewsQuery, postViewsParams);
    }

    public int getViews(String goodsId) {
        String getViewsQuery =
                "select count(*) as views\n" +
                "from Goods g, ViewsGoods vg where g.status != 'delete' and g.goodsId = vg.goodsId and g.goodsId = ?";

        System.out.println("in getViews");

        return this.jdbcTemplate.queryForObject(getViewsQuery, int.class, goodsId);
    }

    //상품 이미지 리스트 조회
    public List<PictureGoodsInfo> getGoodsImgsInfo(String goodsId) {
        String getGoodsImgsQuery = "select p.imgUrl " +
                "from Goods g inner join PicturesGoods p on g.goodsId = p.goodsId " +
                "and g.goodsId = ? and p.status != 'delete'";
        String getGoodsImgsParams = goodsId;

        return this.jdbcTemplate.query(getGoodsImgsQuery,
                (rs, rowNum) -> new PictureGoodsInfo(rs.getString("imgUrl")),
                getGoodsImgsParams);
    }
    //유저 상태 조회
    public int checkUserStatusByUserId(String userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from User where userId = ? and status = 'normal')";
        String checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }
    //유저 상품 리스트 - 카테고리 리스트 가져오기
    public List<Category> getCategoryUser(String userId) {
        String setCategoryUserQuery = "select c.categoryId as categoryId, c.name as categoryName, count(g.categoryId) as counting\n" +
                "from Goods g, Category c where g.categoryId = c.categoryId and g.sellerId = ? and g.status != 'delete' group by categoryId";
        String setCategoryUserParams = userId;

        return this.jdbcTemplate.query(setCategoryUserQuery,
                (rs, rowNum) -> new Category(
                        rs.getInt("categoryId"),
                        rs.getString("categoryName"),
                        rs.getInt("counting")),
                setCategoryUserParams);
    }
    //유저 상품 리스트 - 브랜드 리스트 가져오기
    public List<Brand> getBrandUser(String userId) {
        String setBrandUserQuery = "select g.brandId as brandId, b.brandName, count(g.brandId) as counting from Goods g, Brand b where g.sellerId = ?\n" +
                "and g.brandId is not null and g.brandId = b.brandId and g.status != 'delete' group by brandId";
        String setBrandUserParams = userId;

        return this.jdbcTemplate.query(setBrandUserQuery,
                (rs, rowNum) -> new Brand(
                        rs.getInt("brandId"),
                        rs.getString("brandName"),
                        rs.getInt("counting")),
                setBrandUserParams);
    }
    //키워드 상품 리스트 - 카테고리 리스트 가져오기
    public List<Category> getCategoryKeyword(String keyword) {
        String setCategoryKeywordQuery = "select c.categoryId as categoryId, c.name as categoryName, count(g.categoryId) as counting\n" +
                "from Goods g, Category c where (g.title like ? or g.content like ?) and g.categoryId = c.categoryId and g.status != 'delete' group by categoryId";
        String _keyword = "%" + keyword + "%";
        Object[] setCategoryKeywordParams = new Object[]{_keyword, _keyword};

        return this.jdbcTemplate.query(setCategoryKeywordQuery,
                (rs, rowNum) -> new Category(
                        rs.getInt("categoryId"),
                        rs.getString("categoryName"),
                        rs.getInt("counting")),
                setCategoryKeywordParams);
    }
    //키워드 상품 리스트 - 브랜드 리스트 가져오기
    public List<Brand> getBrandKeyword(String keyword) {
        String setBrandKeywordQuery = "select g.brandId as brandId, b.brandName, count(g.brandId) as counting from Goods g, Brand b\n" +
                "where g.status = 'normal' and (g.title like ? or g.content like ?) and g.brandId is not null and g.brandId = b.brandId group by brandId";
        String _keyword = "%" + keyword + "%";
        Object[] setBrandKeywordParams = new Object[]{_keyword, _keyword};

        return this.jdbcTemplate.query(setBrandKeywordQuery,
                (rs, rowNum) -> new Brand(
                        rs.getInt("brandId"),
                        rs.getString("brandName"),
                        rs.getInt("counting")),
                setBrandKeywordParams);
    }
    //상품 체크
    public int checkGoodsId(String goodsId){
        String checkGoodsIdQuery = "select(exists(select * from Goods where goodsId = ? and status != 'delete'))";
        String checkGoodsIdParams = goodsId;
        return this.jdbcTemplate.queryForObject(checkGoodsIdQuery, int.class, checkGoodsIdParams);
    }
    //유저 상품 리스트 체크
    public int checkGoodsUserId(String userId){
        String checkGoodsUserIdQuery = "select(exists(select * from Goods where sellerId = ? and status != 'delete'))";
        String checkGoodsUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkGoodsUserIdQuery, int.class, checkGoodsUserIdParams);
    }

    //유저 상품 체크
    public int checkGoodsUserId(String userId, String goodsId){
        String checkGoodsUserIdQuery = "select(exists(select * from Goods where status != 'delete' and sellerId = ? and goodsId = ?))";
        Object[] checkGoodsUserIdParams = new Object[]{userId, goodsId};
        return this.jdbcTemplate.queryForObject(checkGoodsUserIdQuery, int.class, checkGoodsUserIdParams);
    }

    //상품 등록
    public int postGoods(String myId, PostGoodsReq postGoodsReq) {
        String postGoodsQuery = "insert into Goods(sellerId, title, content, price," +
                " categoryId, brandId, isFreeShipping, numberOfGoods, isUserGoods, canExchange," +
                " isAdvertising, canBungaePay, regionId)\n" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] postGoodsParams = new Object[]{myId, postGoodsReq.getTitle(), postGoodsReq.getContent(),
                postGoodsReq.getPrice(), postGoodsReq.getCategoryId(), postGoodsReq.getBrandId(),
                postGoodsReq.getIsFreeShipping(), postGoodsReq.getNumberOfGoods(), postGoodsReq.getIsUserGoods(),
                postGoodsReq.getCanExchange(), postGoodsReq.getIsAdvertising(), postGoodsReq.getCanBungaePay(),postGoodsReq.getRegionId()};
        this.jdbcTemplate.update(postGoodsQuery, postGoodsParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public void postWishList(String goodsId) {
        String postWishListQuery = "insert into WishList(goodsId) values(?)";
        String postWishListParams = goodsId;
        this.jdbcTemplate.update(postWishListQuery, postWishListParams);
    }
    public void postGoodsImg(String goodsId, PictureGoods postGoodsImgUrl, String isFirst){
        String postGoodsImgQuery = "insert into PicturesGoods(goodsId, imgUrl, isFirst) values(?,?,?)";
        Object[] postGoodsImgParams = new Object[]{goodsId, postGoodsImgUrl.getImgUrl(), isFirst};
        this.jdbcTemplate.update(postGoodsImgQuery, postGoodsImgParams);
    }

    public void postGoodsTag(String goodsId, Tag tag){
        String postGoodsTagQuery = "insert into TagsGoods(goodsId, content) values(?,?)";
        Object[] postGoodsTagParams = new Object[]{goodsId, tag.getTag()};
        this.jdbcTemplate.update(postGoodsTagQuery, postGoodsTagParams);
    }

    public int patchGoods(String goodsId, PatchGoodsReq patchGoodsReq) {
        String patchGoodsQuery = "update Goods set title = ?, content = ?, price = ?," +
                "categoryId = ?, brandId = ?, isFreeShipping = ?, numberOfGoods = ?, isUserGoods = ?, canExchange = ?," +
                "isAdvertising = ?, canBungaePay = ?, regionId = ? where goodsId = ?";
        Object[] patchGoodsParams = new Object[]{patchGoodsReq.getTitle(), patchGoodsReq.getContent(),
                patchGoodsReq.getPrice(), patchGoodsReq.getCategoryId(), patchGoodsReq.getBrandId(),
                patchGoodsReq.getIsFreeShipping(), patchGoodsReq.getNumberOfGoods(), patchGoodsReq.getIsUserGoods(),
                patchGoodsReq.getCanExchange(), patchGoodsReq.getIsAdvertising(), patchGoodsReq.getCanBungaePay(),patchGoodsReq.getRegionId(), goodsId};
        return this.jdbcTemplate.update(patchGoodsQuery,patchGoodsParams);
    }

    public void postKeyword(String keyword, String myId) {
        String postKeywordQuery = "insert SearchLog(content, userId) values(?,?)";
        Object[] postKeywordParams = new Object[]{keyword, myId};

        this.jdbcTemplate.update(postKeywordQuery, postKeywordParams);
    }

    //기존 img 제거(상탯값 변환)
    public void patchGoodsImg(String goodsId){
        String patchGoodsImgQuery = "update PicturesGoods set status = 'delete' where goodsId = ?";
        String patchGoodsImgParams = goodsId;
        this.jdbcTemplate.update(patchGoodsImgQuery, patchGoodsImgParams);
    }

    //기존 tg 제거(상탯값 변환)
    public void patchGoodsTag(String goodsId){
        String patchGoodsTagQuery = "update TagsGoods set status = 'delete' where goodsId = ?";
        String patchGoodsTagParams = goodsId;
        this.jdbcTemplate.update(patchGoodsTagQuery, patchGoodsTagParams);
    }

    //상품 제거(상탯값 변환)
    public void deleteGoods(String goodsId){
        String deleteGoodsQuery = "update Goods set status = 'delete' where goodsId = ?";
        String deleteGoodsParams = goodsId;
        this.jdbcTemplate.update(deleteGoodsQuery, deleteGoodsParams);
    }
}
