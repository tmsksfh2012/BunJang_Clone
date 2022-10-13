package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public List<GetUserRes> getUsers(){
//        String getUsersQuery = "select * from User";
//        return this.jdbcTemplate.query(getUsersQuery,
//                (rs,rowNum) -> new GetUserRes(
//                        rs.getInt("userId"),
//                        rs.getString("name"),
//                        rs.getString("email"),
//                        rs.getString("password"),
//                        rs.getString("phone"))
//                );
//    }

//    public List<GetUserRes> getUsersByName(String name){
//        String getUsersByNameQuery = "select * from User where name =?";
//        String getUsersByNameParams = name;
//        return this.jdbcTemplate.query(getUsersByNameQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userId"),
//                        rs.getString("name"),
//                        rs.getString("email"),
//                        rs.getString("password"),
//                        rs.getString("phone")),
//                getUsersByNameParams);
//    }
//
//    public GetUserRes getUser(int userIdx){
//        String getUserQuery = "select * from User where userId = ?";
//        int getUserParams = userIdx;
//        return this.jdbcTemplate.queryForObject(getUserQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userId"),
//                        rs.getString("name"),
//                        rs.getString("email"),
//                        rs.getString("password"),
//                        rs.getString("phone")),
//                getUserParams);
//    }
    

//    public int createUser(UserEmail user){
//        String createUserQuery = "insert into User (name, nickName, email, loginType) VALUES (?, ?, 1)";
//        Object[] createUserParams = new Object[]{user.getName(), user.getNickName(), user.getEmail()};
//        this.jdbcTemplate.update(createUserQuery, createUserParams);
//
//        String lastInserIdQuery = "select last_insert_id()";
//        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
//    }

    public GetUserDetailRes getUserDetail(String userId) {
        String getUserDetailQuery =
                "select uu.nickName as nickName, uu.imgUrl as imgUrl,\n" +
                        "if(uu.content is null, '', uu.content) as content,\n" +
                        "count(if(flr.followee = ?, flr.followee, null)) as follower,\n" +
                        "count(if(flr.follower = ?, flr.follower, null)) as following,\n" +
                        "timestampdiff(day, uu.createdAt, current_timestamp) as gapCreatedAt from User u\n" +
                        "left outer join FollowerList flr on u.userId = flr.followee\n" +
                        "left outer join User uu on uu.userId = u.userId where uu.userId = ?";

        Object[] getUserDetailParams = new Object[]{userId, userId, userId};

        return this.jdbcTemplate.queryForObject(getUserDetailQuery,
                (rs, rowNum) -> new GetUserDetailRes(
                        rs.getString("nickName"),
                        rs.getString("imgUrl"),
                        rs.getString("content"),
                        rs.getInt("follower"),
                        rs.getInt("following"),
                        rs.getInt("gapCreatedAt")),
                getUserDetailParams);

    }
    public UserDetailStarTransaction getUserDetailStarAndTransaction(String userId) {
        String getUserDetailStarAndTransactionQuery =
                "select round(avg(tr.starRating), 1) as star, count(th.sellerId) as transaction\n" +
                "from TransactionHistory th\n" +
                "left outer join TransactionReview tr on th.thId = tr.thId\n" +
                "where th.sellerId = ?";

        String getUserDetailStarAndTransactionParams = userId;
        return this.jdbcTemplate.queryForObject(getUserDetailStarAndTransactionQuery,
                (rs, rowNum) -> new UserDetailStarTransaction(
                        rs.getFloat("star"),
                        rs.getInt("transaction")),
                getUserDetailStarAndTransactionParams);

    }
    //카카오 회원가입
    public int createKakaoUser(UserKakao user) {
        String createKakaoUserQuery = "insert into User (nickName, email) VALUES (?, ?)";
        Object[] createKakaoUserParams = new Object[]{user.getNickName(), user.getEmail()};
        this.jdbcTemplate.update(createKakaoUserQuery, createKakaoUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

//    public int modifyUserName(PatchUserReq patchUserReq){
//        String modifyUserNameQuery = "update User set name = ? where userId = ? ";
//        Object[] modifyUserNameParams = new Object[]{patchUserReq.getName(), patchUserReq.getUserId()};
//
//        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
//    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ? and status ='normal')";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public User checkEmailUser(String email) {
        String checkEmailUserQuery = "select userId as userIdx, nickName, email from User where email = ?";
        String chekEmailUserParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailUserQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("email")),
                chekEmailUserParams
        );
    }

    public int checkBanUser(String myId, String userId) {
        String checkBlockQuery = "select exists(select * from BlockList " +
                "where blockerId = ? and blockedUserId = ? and status = 'normal')";
        Object[] checkBlockParams = new Object[]{myId, userId};
        return this.jdbcTemplate.queryForObject(checkBlockQuery, int.class, checkBlockParams);
    }

    public void postBanUser(String myId, String userId) {
        String postBanUserQuery = "insert into BlockList (blockerId, blockedUserId) VALUES (?, ?)";
        Object[] postBanUserParams = new Object[]{myId, userId};
        this.jdbcTemplate.update(postBanUserQuery, postBanUserParams);
    }
    public void patchBanUser(String myId, String userId) {
        String postBanUserQuery = "update BlockList set status = 'unban' where blockerId = ? and blockedUserId = ?";
        Object[] postBanUserParams = new Object[]{myId, userId};
        this.jdbcTemplate.update(postBanUserQuery, postBanUserParams);
    }

    public void deleteUser(String myId) {
        String deleteUserQuery = "update User set status = 'delete' where userId = ?";
        String deleteUserParams = myId;
        this.jdbcTemplate.update(deleteUserQuery, deleteUserParams);
    }

    public void patchMy(String myId, PatchUserReq patchUserReq) {
        String patchMyQuery = "update User set nickName = ?, imgUrl = ?, content = ? where userId = ?";
        Object[] patchMyParams = new Object[]{patchUserReq.getNickName(),
                patchUserReq.getImgUrl(), patchUserReq.getContent(), myId};
        this.jdbcTemplate.update(patchMyQuery, patchMyParams);
    }

    public int checkUserStatusByUserId(String userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from User where userId = ? and status = 'normal')";
        String checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }

    public int checkUserId(Integer userId){
        String checkUserIdQuery = "select exists(select userId from User where userId = ? and status = 'normal')";
        Integer checkUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserIdParams);
    }

    public GetChatUserRes getChatUserInfo(int goodsId, int userId) {
        String getChatUserInfoQuery = "select User.nickName, User.imgUrl as userImg, IFNULL(FORMAT((ROUND(AVG(TR.starRating))), 1), '0.0') as rating, CONCAT('(', COUNT(distinct TH.thId), ')') as countOfReview,\n" +
                "       CONCAT('지금까지 ', COUNT(distinct TH.thId), '개의 상품을 판매했어요.') as transactionHistory,\n" +
                "       G.title as goodsTitle, CONCAT(FORMAT(G.price, 0) , '원') as goodsPrice, PG.imgUrl as goodsImg\n" +
                "from User\n" +
                "left join Goods G on User.userId = G.sellerId and goodsId = ?\n" +
                "left join TransactionHistory TH on TH.sellerId = User.userId\n" +
                "left join TransactionReview TR on TH.thId = TR.thId\n" +
                "left join (select goodsId, imgUrl from PicturesGoods where status = 'normal' and isFirst = 'Y') PG on G.goodsId = PG.goodsId\n" +
                "where userId = ?";
        Object[] getChatUserInfoParams = new Object[]{goodsId, userId};
        return this.jdbcTemplate.queryForObject(getChatUserInfoQuery, (rs,rowNum)-> new GetChatUserRes(
                rs.getString("nickName"),
                rs.getString("userImg"),
                rs.getString("rating"),
                rs.getString("countOfReview"),
                rs.getString("transactionHistory"),
                rs.getString("goodsTitle"),
                rs.getString("goodsPrice"),
                rs.getString("goodsImg")), getChatUserInfoParams);
    }
}
