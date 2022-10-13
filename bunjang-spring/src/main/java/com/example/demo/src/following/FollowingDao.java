package com.example.demo.src.following;

import com.example.demo.src.following.model.FollowingGoods;
import com.example.demo.src.following.model.GetFollowerRes;
import com.example.demo.src.following.model.GetFollowingRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FollowingDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //유저 상태 조회
    public int checkUserStatusByUserId(String userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from User where userId = ? and status = 'normal')";
        String checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }

    public int checkFollowingStatusByUserId(String myId, String userId) {
        String checkFollowingStatusByUserIdQuery = "select exists(select * from FollowerList where follower = ? and followee = ? and status = 'normal')";
        Object[] checkFollowingStatusByUserIdParams = new Object[]{myId, userId};
        return this.jdbcTemplate.queryForObject(checkFollowingStatusByUserIdQuery, int.class, checkFollowingStatusByUserIdParams);
    }

    public void postFollowing(String myId, String userId) {
        String postFollowingQuery = "insert into FollowerList(follower, followee) values(?,?)";
        Object[] postFollowingParams = new Object[]{myId, userId};

        this.jdbcTemplate.update(postFollowingQuery, postFollowingParams);
    }

    public void deleteFollowing(String myId, String userId) {
        String deleteFollowingQuery = "update FollowerList set status = 'delete' where follower = ? and followee = ?";
        Object[] deleteFollowingParams = new Object[]{myId, userId};

        this.jdbcTemplate.update(deleteFollowingQuery, deleteFollowingParams);
    }


    //유저가 팔로우
    public List<GetFollowingRes> getFolloweeRes(String userId) {
        String getFolloweeQuery = "select u.userId, u.nickName as nickName, u.imgUrl as userImgUrl,\n" +
                "(select count(if(g.goodsId, g.goodsId, null)) from Goods g where g.sellerId = u.userId) as countGoods,\n" +
                "(select count(if(fl.followee, fl.followee, null))from FollowerList fl where u.userId = fl.followee) as countFollowing\n" +
                "from User u, FollowerList fl where u.userId = fl.followee and fl.follower = ? and fl.status = 'normal' group by userId";
        String getFolloweeParams = userId;

        return this.jdbcTemplate.query(getFolloweeQuery,
                (rs,rowNum) -> new GetFollowingRes(
                        rs.getInt("userId"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getInt("countGoods"),
                        rs.getInt("countFollowing")),
                getFolloweeParams);
    }

    //유저를 팔로우
    public List<GetFollowerRes> getFollowerRes(String userId) {
        String getFolloweeQuery = "select u.userId, u.nickName as nickName, u.imgUrl as userImgUrl,\n" +
                "(select count(if(g.goodsId, g.goodsId, null)) from Goods g where g.sellerId = u.userId) as countGoods,\n" +
                "(select count(if(fl.followee, fl.followee, null))from FollowerList fl where u.userId = fl.followee) as countFollower\n" +
                "from User u, FollowerList fl where u.userId = fl.follower and fl.followee = ? and fl.status = 'normal' group by userId";
        String getFolloweeParams = userId;

        return this.jdbcTemplate.query(getFolloweeQuery,
                (rs,rowNum) -> new GetFollowerRes(
                        rs.getInt("userId"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getInt("countGoods"),
                        rs.getInt("countFollower")),
                getFolloweeParams);
    }


    public List<FollowingGoods> getFolloweeGoods(String userId) {
        String getFolloweeGoodsQuery = "select g.goodsId, pg.imgUrl, CONCAT(FORMAT(g.price, 0) , '원') as price from Goods g\n" +
                "inner join PicturesGoods pg on g.goodsId = pg.goodsId where g.sellerId = ? and pg.status = 'normal'\n" +
                "and pg.isFirst = 'Y' order by g.createdAt desc limit 3";
        String getFolloweeGoodsParams = userId;

        return this.jdbcTemplate.query(getFolloweeGoodsQuery,
                (rs,rowNum) -> new FollowingGoods(
                        rs.getInt("goodsId"),
                        rs.getString("imgUrl"),
                        rs.getString("price")),
                getFolloweeGoodsParams);
    }
}
