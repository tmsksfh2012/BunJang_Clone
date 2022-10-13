package com.example.demo.src.wishList;

import com.example.demo.src.goods.model.Brand;
import com.example.demo.src.wishList.model.GetWishListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class WishListDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetWishListRes> getWishList(String userId){
        String getWishListQuery = "select g.goodsId as goodsId, g.title as title, CONCAT(FORMAT(g.price, 0) , '원') as price,\n" +
                "       g.canBungaePay as canBungaePay, p.imgUrl,\n" +
                "       g.status as status, u.nickName as sellerNickName, u.imgUrl as sellerImg\n" +
                "from Goods g\n" +
                "inner join User u on g.sellerId = u.userId\n" +
                "inner join PicturesGoods p on g.goodsId = p.goodsId\n" +
                "left outer join WishList w on g.goodsId = w.goodsId\n" +
                "left outer join Wish ws on w.wlId = ws.wlId\n" +
                "where ws.userId = ? and g.status != 'delete' and ws.status != 'delete' and w.status != 'delete' group by g.goodsId";
        String getWishListParams = userId;

        return this.jdbcTemplate.query(getWishListQuery,
                (rs, rowNum) -> new GetWishListRes(
                        rs.getInt("goodsId"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getString("canBungaePay"),
                        rs.getString("imgUrl"),
                        rs.getString("status"),
                        rs.getString("sellerNickName"),
                        rs.getString("sellerImg")),
                getWishListParams);
    }
    public void postWishList(String myId, String goodsId) {
        String getWishListQuery = "select wlId from WishList where goodsId = ? and status = 'normal'";
        int wlId = this.jdbcTemplate.queryForObject(getWishListQuery, int.class, goodsId);



        String postWishListQuery = "insert into Wish(userId,wlId) values (?,?)";
        Object[] postWishListParams = new Object[]{myId, wlId};
        this.jdbcTemplate.update(postWishListQuery, postWishListParams);
    }

    //상품 체크
    public int checkGoodsId(String goodsId){
        String checkGoodsIdQuery = "select(exists(select * from Goods where goodsId = ? and status = 'normal'))";
        String checkGoodsIdParams = goodsId;
        return this.jdbcTemplate.queryForObject(checkGoodsIdQuery, int.class, checkGoodsIdParams);
    }
    public int checkUserStatusByUserId(String userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from User where userId = ? and status = 'normal')";
        String checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }

    public int checkGoodsUserId(String userId, String goodsId){
        String checkGoodsUserIdQuery = "select(exists(select * from Goods where sellerId = ? and goodsId = ? and status != 'delete'))";
        Object[] checkGoodsUserIdParams = new Object[]{userId, goodsId};
        return this.jdbcTemplate.queryForObject(checkGoodsUserIdQuery, int.class, checkGoodsUserIdParams);
    }

    public int checkWishStatusByUserId(String myId, String goodsId) {
        String getWishListQuery = "select wlId from WishList where goodsId = ? and status = 'normal'";
        int wlId = this.jdbcTemplate.queryForObject(getWishListQuery, int.class, goodsId);

        String checkWishStatusByUserIdQuery = "select exists(select * from Wish where userId = ? and wlId = ? and status = 'normal')";
        Object[] checkWishStatusByUserIdParams = new Object[]{myId, wlId};
        return this.jdbcTemplate.queryForObject(checkWishStatusByUserIdQuery, int.class, checkWishStatusByUserIdParams);
    }
    public void deleteWishList(String myId, String goodsId) {
        String getWishListQuery = "select wlId from WishList where goodsId = ? and status = 'normal'";
        int wlId = this.jdbcTemplate.queryForObject(getWishListQuery, int.class, goodsId);

        String deleteWishListQuery = "update Wish set status = 'delete' where userId = ? and wlId = ?";
        Object[] deleteWishListParams = new Object[]{myId, wlId};
        this.jdbcTemplate.update(deleteWishListQuery, deleteWishListParams);
    }
}
