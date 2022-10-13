package com.example.demo.src.chatting;

import com.example.demo.src.chatting.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class
ChattingDao {

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkCrId(int crId){
        String checkCrIDQuery = "select exists(select crId from ChatRooms " +
                "where crId = ? and status <> 'deleted')";
        int checkCrIdParams = crId;
        return this.jdbcTemplate.queryForObject(checkCrIDQuery,
                int.class,
                checkCrIdParams);
    }

    public int checkCrId(int crId, int buyerId, int sellerId){
        String checkCrIDQuery = "select exists(select crId from ChatRooms " +
                "where crId = ? and buyerId = ? and sellerId = ? and status <> 'deleted')";
        Object[] checkCrIdParams = new Object[]{crId, buyerId, sellerId};
        return this.jdbcTemplate.queryForObject(checkCrIDQuery,
                int.class,
                checkCrIdParams);
    }

    public int checkRoomByGoodsId(int buyerId, int sellerId, int goodsId) {
        String checkRoomByGoodsIdQuery = "select exists(select crId from ChatRooms " +
                "where buyerId = ? and sellerId = ? and goodsId = ? and status = 'normal')";
        Object[] checkRoomByGoodsIdParams = new Object[]{buyerId, sellerId, goodsId};
        return this.jdbcTemplate.queryForObject(checkRoomByGoodsIdQuery,
                int.class,
                checkRoomByGoodsIdParams);
    }

    public int checkFromToId(int crId, int fromId, int toId){
        String checkCrIDQuery = "select exists(select crId, buyerId, sellerId from ChatRooms " +
                "where crId = ? and ((buyerId = ? and sellerId = ?) or (buyerId = ? and sellerId = ?)))";
        Object[] checkCrIdParams = new Object[]{crId, fromId, toId, toId, fromId};
        return this.jdbcTemplate.queryForObject(checkCrIDQuery,
                int.class,
                checkCrIdParams);
    }

    public int checkFromToId(int crId, int userId){
        String checkCrIDQuery = "select exists(select crId, buyerId, sellerId from ChatRooms " +
                "where crId = ? and (buyerId = ? or sellerId = ?))";
        Object[] checkCrIdParams = new Object[]{crId, userId, userId};
        return this.jdbcTemplate.queryForObject(checkCrIDQuery,
                int.class,
                checkCrIdParams);
    }

    public int checkTHByCrId(int crId) {
        String checkTHByCrIdQuery = "select exists (select thId from TransactionHistory\n" +
                "inner join Goods G on TransactionHistory.goodsId = G.goodsId\n" +
                "inner join User U on (TransactionHistory.buyerId = U.userId or TransactionHistory.sellerId = U.userId)\n" +
                "inner join ChatRooms CR on G.goodsId = CR.goodsId\n" +
                "where TransactionHistory.status <> 'deleted' and crId = ?)";
        int checkTHByCrIdParams = crId;
        return this.jdbcTemplate.queryForObject(checkTHByCrIdQuery,
                int.class,
                checkTHByCrIdParams);
    }

    public GetRoomsIdxReq getIdx(int crId) {
        String getIdxQuery = "select buyerId, sellerId, goodsId from ChatRooms where crId = ?";
        int getIdxParams = crId;
        return this.jdbcTemplate.queryForObject(getIdxQuery,
                (rs, rowNum) -> new GetRoomsIdxReq(
                        rs.getInt("buyerId"),
                        rs.getInt("sellerId"),
                        rs.getInt("goodsId")
                )
                , getIdxParams);
    }

    public int createChatRoom(PostChatRoomsReq postChatRoomsReq) {
        String createChatRoomQuery = "insert into ChatRooms (buyerId, sellerId, goodsId) VALUES (?,?,?)";
        Object[] createChatRoomParams = new Object[]
                {postChatRoomsReq.getBuyerId(), postChatRoomsReq.getSellerId(), postChatRoomsReq.getGoodsId()};
        this.jdbcTemplate.update(createChatRoomQuery, createChatRoomParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int deleteChatRoom(int crId, PatchChatRoomsReq patchChatRoomsReq) {
        String deleteChatRoomQuery = "update ChatRooms set status = ? where crId = ?";
        Object[] deleteChatRoomParams = new Object[]{patchChatRoomsReq.getStatus(), crId};

        return this.jdbcTemplate.update(deleteChatRoomQuery, deleteChatRoomParams);
    }

    @Transactional
    public int alarmToggleChatRoomOnBuyer(int crId) {
        String alarmToggleChatRoomOnBuyerQuery = "update ChatRooms set buyerAlarm = 1 - buyerAlarm where crId = ?";
        this.jdbcTemplate.update(alarmToggleChatRoomOnBuyerQuery, crId);

        String alarmStatusQuery = "select buyerAlarm from ChatRooms where crId = ?";
        return this.jdbcTemplate.queryForObject(alarmStatusQuery,
                int.class,
                crId);
    }

    @Transactional
    public int alarmToggleChatRoomOnSeller(int crId) {
        String alarmToggleChatRoomOnSellerQuery = "update ChatRooms set sellerAlarm = 1 - sellerAlarm where crId = ?";

        this.jdbcTemplate.update(alarmToggleChatRoomOnSellerQuery, crId);
        String alarmStatusQuery = "select sellerAlarm from ChatRooms where crId = ?";
        return this.jdbcTemplate.queryForObject(alarmStatusQuery,
                int.class,
                crId);
    }

    public List<GetRoomsRes> getRooms(int userId) {
        String getRoomsQuery = "select ChatRooms.crId, ifnull(User.imgUrl, 'NoImg.jpg') as roomImgUrl, nickName as roomName, forLastMessage.chatsId, Goods.goodsId, PG.imgUrl as thumbnail, \n" +
                "                                       case when TIMESTAMPDIFF(DAY, forLastMessage.lastMessageTime, current_timestamp) >= 1 then\n" +
                "                                           CONCAT(DATE_FORMAT(forLastMessage.lastMessageTime,'%c'),'월 ',DATE_FORMAT(forLastMessage.lastMessageTime,'%e'),'일') \n" +
                "                                        else CONCAT(if(DATE_FORMAT(forLastMessage.lastMessageTime, '%p') = 'AM', '오전 ', '오후 '),DATE_FORMAT(forLastMessage.lastMessageTime, '%H:%i')) end as lastMessageTime, \n" +
                "                                       forLastMessage.lastContent as chatContent\n" +
                "                                from ChatRooms  \n" +
                "                                inner join User on (ChatRooms.sellerId = User.userId or ChatRooms.buyerId = User.userId) and User.userId <> ?\n" +
                "                                inner join Goods on ChatRooms.sellerId = Goods.sellerId\n" +
                "                                left join (select goodsId, imgUrl from PicturesGoods where status = 'normal' and isFirst = 'Y') PG on Goods.goodsId = PG.goodsId\n" +
                "                                inner join (select Chats.crId, Chats.chatsId, Chats.content as lastContent, Chats.createdAt as lastMessageTime  \n" +
                "                                            ,Chats.type as lastMessageType from Chats  \n" +
                "                                inner join (select Chats.crId, max(chatsId) as maxChatsId from Chats group by crId) lastMessage  \n" +
                "                                where maxChatsId = Chats.chatsId) as forLastMessage on ChatRooms.crId = forLastMessage.crId  \n" +
                "                                where (ChatRooms.sellerId = ? or ChatRooms.buyerId = ?) and ChatRooms.status <> 'deleted'\n" +
                "                                group by forLastMessage.crId";
        Object[] getRoomsParams = new Object[]{userId, userId, userId};
        return this.jdbcTemplate.query(getRoomsQuery,
                (rs, rowNum) -> new GetRoomsRes(
                        rs.getInt("crId"),
                        rs.getString("roomImgUrl"),
                        rs.getString("roomName"),
                        rs.getInt("chatsId"),
                        rs.getInt("goodsId"),
                        rs.getString("thumbnail"),
                        rs.getString("lastMessageTime"),
                        rs.getString("chatContent")
                )
                ,getRoomsParams);
    }

    public List<GetRoomsRes> getRoomsForBuy(int userId) {
        String getRoomsQuery = "select ChatRooms.crId, ifnull(User.imgUrl, 'NoImg.jpg') as roomImgUrl, nickName as roomName, forLastMessage.chatsId, Goods.goodsId, PG.imgUrl as thumbnail,\n" +
                "                                       case when TIMESTAMPDIFF(DAY, forLastMessage.lastMessageTime, current_timestamp) >= 1 then\n" +
                "                                           CONCAT(DATE_FORMAT(forLastMessage.lastMessageTime,'%c'),'월 ',DATE_FORMAT(forLastMessage.lastMessageTime,'%e'),'일')\n" +
                "                                        else CONCAT(if(DATE_FORMAT(forLastMessage.lastMessageTime, '%p') = 'AM', '오전 ', '오후 '),DATE_FORMAT(forLastMessage.lastMessageTime, '%H:%i')) end as lastMessageTime,\n" +
                "                                       forLastMessage.lastContent as chatContent\n" +
                "                                from ChatRooms\n" +
                "                                inner join User on (ChatRooms.sellerId = User.userId or ChatRooms.buyerId = User.userId) and User.userId <> ?\n" +
                "                                inner join Goods on ChatRooms.sellerId = Goods.sellerId\n" +
                "                                left join (select goodsId, imgUrl from PicturesGoods where status = 'normal' and isFirst = 'Y') PG on Goods.goodsId = PG.goodsId\n" +
                "                                inner join (select Chats.crId, Chats.chatsId, Chats.content as lastContent, Chats.createdAt as lastMessageTime\n" +
                "                                            ,Chats.type as lastMessageType from Chats\n" +
                "                                inner join (select Chats.crId, max(chatsId) as maxChatsId from Chats group by crId) lastMessage\n" +
                "                                where maxChatsId = Chats.chatsId) as forLastMessage on ChatRooms.crId = forLastMessage.crId\n" +
                "                                where ChatRooms.buyerId = ? and ChatRooms.status <> 'deleted'\n" +
                "                                group by forLastMessage.crId";
        Object[] getRoomsParams = new Object[]{userId, userId};
        return this.jdbcTemplate.query(getRoomsQuery,
                (rs, rowNum) -> new GetRoomsRes(
                        rs.getInt("crId"),
                        rs.getString("roomImgUrl"),
                        rs.getString("roomName"),
                        rs.getInt("chatsId"),
                        rs.getInt("goodsId"),
                        rs.getString("thumbnail"),
                        rs.getString("lastMessageTime"),
                        rs.getString("chatContent")
                )
                ,getRoomsParams);
    }

    public List<GetRoomsRes> getRoomsForSell(int userId) {
        String getRoomsQuery = "select ChatRooms.crId, ifnull(User.imgUrl, 'NoImg.jpg') as roomImgUrl, nickName as roomName, forLastMessage.chatsId, Goods.goodsId, PG.imgUrl as thumbnail,\n" +
                "                                       case when TIMESTAMPDIFF(DAY, forLastMessage.lastMessageTime, current_timestamp) >= 1 then\n" +
                "                                           CONCAT(DATE_FORMAT(forLastMessage.lastMessageTime,'%c'),'월 ',DATE_FORMAT(forLastMessage.lastMessageTime,'%e'),'일')\n" +
                "                                        else CONCAT(if(DATE_FORMAT(forLastMessage.lastMessageTime, '%p') = 'AM', '오전 ', '오후 '),DATE_FORMAT(forLastMessage.lastMessageTime, '%H:%i')) end as lastMessageTime,\n" +
                "                                       forLastMessage.lastContent as chatContent\n" +
                "                                from ChatRooms\n" +
                "                                inner join User on (ChatRooms.sellerId = User.userId or ChatRooms.buyerId = User.userId) and User.userId <> ?\n" +
                "                                inner join Goods on ChatRooms.sellerId = Goods.sellerId\n" +
                "                                left join (select goodsId, imgUrl from PicturesGoods where status = 'normal' order by PicturesGoods.pgId limit 1) PG on Goods.goodsId = PG.goodsId\n" +
                "                                inner join (select Chats.crId, Chats.chatsId, Chats.content as lastContent, Chats.createdAt as lastMessageTime\n" +
                "                                            ,Chats.type as lastMessageType from Chats\n" +
                "                                inner join (select Chats.crId, max(chatsId) as maxChatsId from Chats group by crId) lastMessage\n" +
                "                                where maxChatsId = Chats.chatsId) as forLastMessage on ChatRooms.crId = forLastMessage.crId\n" +
                "                                where ChatRooms.sellerId = ? and ChatRooms.status <> 'deleted'\n" +
                "                                group by forLastMessage.crId;;";
        Object[] getRoomsParams = new Object[]{userId, userId};
        return this.jdbcTemplate.query(getRoomsQuery,
                (rs, rowNum) -> new GetRoomsRes(
                        rs.getInt("crId"),
                        rs.getString("roomImgUrl"),
                        rs.getString("roomName"),
                        rs.getInt("chatsId"),
                        rs.getInt("goodsId"),
                        rs.getString("thumbnail"),
                        rs.getString("lastMessageTime"),
                        rs.getString("chatContent")
                )
                ,getRoomsParams);
    }

    public GetRoomsInfoReq getRoomInfo(int crId) {
        String getRoomInfoQuery = "select distinct U.nickName, PG.imgUrl as goodsImg, CONCAT(FORMAT(G.price, 0) , '원') as goodsPrice, G.title as goodsTitle, G.canBungaePay as goodsCanBungaePay\n" +
                "from ChatRooms\n" +
                "inner join Goods G on ChatRooms.goodsId = G.goodsId\n" +
                "left join (select pgId, imgUrl, goodsId from PicturesGoods where isFirst = 'Y') PG on G.goodsId = PG.goodsId\n" +
                "inner join User U on G.sellerId = U.userId\n" +
                "where crId = ?";
        Object[] getRoomInfoParams = new Object[]{crId};
        return this.jdbcTemplate.queryForObject(getRoomInfoQuery,
                (rs, rowNum) -> new GetRoomsInfoReq(
                        rs.getString("nickName"),
                        rs.getString("goodsImg"),
                        rs.getString("goodsPrice"),
                        rs.getString("goodsTitle"),
                        rs.getString("goodsCanBungaePay")
                )
                , getRoomInfoParams);
    }

    public List<GetChatsRes> getChats(int crId) {
        String getChatsQuery = "select CONCAT(if(DATE_FORMAT(C.createdAt, '%p') = 'PM', '오후 ','오전 '), DATE_FORMAT(C.createdAt, '%h : %i')) as messageTime, nickName,fromId, C.status, imgUrl, chatsId,\n" +
                "                C.content, C.type, isChecked \n" +
                "                from ChatRooms \n" +
                "                inner join Chats C on ChatRooms.crId = C.crId\n" +
                "                inner join User on C.fromId = User.userId \n" +
                "                where ChatRooms.crId = ?  \n" +
                "                order by chatsId";
        int getChatsParam = crId;
        return this.jdbcTemplate.query(getChatsQuery,
                (rs, rowNum) -> new GetChatsRes(
                        rs.getString("messageTime"),
                        rs.getString("nickName"),
                        rs.getInt("fromId"),
                        rs.getString("status"),
                        rs.getString("imgUrl"),
                        rs.getInt("chatsId"),
                        rs.getString("content"),
                        rs.getString("type"),
                        rs.getString("isChecked")
                )
                , getChatsParam);
    }

    public int createChat(PostChatReq postChatReq) {
        String createChatQuery = "insert into Chats (crId, content, fromId, toId, type) VALUES (?,?,?,?,?)";
        Object[] createChatParams = new Object[]
                {postChatReq.getCrId(), postChatReq.getContent(), postChatReq.getFromId(), postChatReq.getToId(),postChatReq.getType()};
        this.jdbcTemplate.update(createChatQuery, createChatParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    @Transactional
    public PostMakeDealRes makeDeal(GetRoomsIdxReq getRoomsIdxReq, PostMakeDealReq postMakeDealReq) {
        String makeDealReqQuery = "insert into TransactionHistory " +
                "(buyerId, sellerId, goodsId, meetAt, regionContent, askPrice) VALUES (?,?,?,?,?,?)";
        Object[] makeDealReqParams = new Object[]
                {getRoomsIdxReq.getBuyerId(), getRoomsIdxReq.getSellerId(),getRoomsIdxReq.getGoodsId(),
                postMakeDealReq.getMeetAt(),postMakeDealReq.getRegionContent(), postMakeDealReq.getAskPrice()};
        this.jdbcTemplate.update(makeDealReqQuery, makeDealReqParams);

        String lastInsertQuery = "select last_insert_id()";
        int lastInsertIdx = this.jdbcTemplate.queryForObject(lastInsertQuery,int.class);

        String makeDealResQuery = "select G.title as goodsTitle, FORMAT(askPrice, 0) as askPrice from TransactionHistory\n" +
                "inner join Goods G on TransactionHistory.goodsId = G.goodsId\n" +
                "where thId = ?";
        return this.jdbcTemplate.queryForObject(makeDealResQuery,
                (rs, rowNum) -> new PostMakeDealRes(
                        lastInsertIdx,
                        "직거래를 요청합니다.",
                        "만나서 거래해요",
                        rs.getString("goodsTitle"),
                        "직거래",
                        rs.getString("askPrice") +"원")
                , lastInsertIdx);
    }

    @Transactional
    public PostMakeDealRes patchDeal(int crId, PostMakeDealReq postMakeDealReq) {
        String thIdQuery = "select thId from TransactionHistory\n" +
                "inner join Goods G on TransactionHistory.goodsId = G.goodsId\n" +
                "inner join ChatRooms CR on G.goodsId = CR.goodsId\n" +
                "where TransactionHistory.status <> 'deleted' and crId = ?";
        int thId = this.jdbcTemplate.queryForObject(thIdQuery, int.class, crId);

        String patchDealQuery = "update TransactionHistory\n" +
                "set askPrice = ?, meetAt = ?, regionContent = ?\n" +
                "where thId = ?";
        Object[] patchDealParams = new Object[]
                {postMakeDealReq.getAskPrice(), postMakeDealReq.getMeetAt(), postMakeDealReq.getRegionContent(), thId};
        this.jdbcTemplate.update(patchDealQuery, patchDealParams);

        String returnValueQuery = "select G.title as goodsTitle, FORMAT(askPrice, 0) as askPrice from TransactionHistory\n" +
                "inner join Goods G on TransactionHistory.goodsId = G.goodsId\n" +
                "where thId = ?";


        return this.jdbcTemplate.queryForObject(returnValueQuery,
                (rs, rowNum) -> new PostMakeDealRes(
                        thId,
                        "거래가 수정되었습니다.",
                        "만나서 거래해요",
                        rs.getString("goodsTitle"),
                        "직거래",
                        rs.getString("askPrice") +"원")
                , thId);
    }

    @Transactional
    public PostMakeDealRes deleteDeal(int crId) {
        String thIdQuery = "select thId from TransactionHistory\n" +
                "inner join Goods G on TransactionHistory.goodsId = G.goodsId\n" +
                "inner join ChatRooms CR on G.goodsId = CR.goodsId\n" +
                "where TransactionHistory.status <> 'deleted' and crId = ?";
        int thId = this.jdbcTemplate.queryForObject(thIdQuery, int.class, crId);

        String deleteDealQuery = "update TransactionHistory set status = 'deleted' where thId = ?";
        int deleteDealParams = thId;
        this.jdbcTemplate.update(deleteDealQuery, deleteDealParams);

        String returnValueQuery = "select G.title as goodsTitle, FORMAT(askPrice, 0) as askPrice from TransactionHistory\n" +
                "inner join Goods G on TransactionHistory.goodsId = G.goodsId\n" +
                "where thId = ?";

        return this.jdbcTemplate.queryForObject(returnValueQuery,
                (rs, rowNum) -> new PostMakeDealRes(
                        thId,
                        "거래가 취소되었습니다.",
                        "만나서 거래해요",
                        rs.getString("goodsTitle"),
                        "직거래",
                        rs.getString("askPrice") +"원")
                , thId);
    }

    public int checkDealExist(int userId, int crId) {
        String checkDealExistQuery = "select exists(select thId, G.goodsId, title as goodsTitle, PG.imgUrl as goodsImg, isFreeShipping, askPrice as askPrice, meetAt,\n" +
                "       regionContent, email from TransactionHistory\n" +
                "inner join Goods G on TransactionHistory.goodsId = G.goodsId\n" +
                "left join (select * from PicturesGoods where isFirst = 'Y') PG on G.goodsId = PG.goodsId\n" +
                "inner join ChatRooms CR on G.goodsId = CR.goodsId\n" +
                "inner join User U on (CR.buyerId = U.userId or CR.sellerId = U.userId) and U.userId <> ?\n" +
                "where TransactionHistory.status <> 'deleted' and crId = ? and (TransactionHistory.buyerId = ? or TransactionHistory.sellerId = ?))";
        Object[] checkDealExistParams = new Object[]{ userId, crId, userId, userId };
        return this.jdbcTemplate.queryForObject(checkDealExistQuery,
                int.class,
                checkDealExistParams);
    }

    public GetMakeDealRes getDealInfo(int userId, int crId) {
        String getDealInfoQuery = "select thId, G.goodsId, title as goodsTitle, PG.imgUrl as goodsImg, isFreeShipping, askPrice as askPrice, meetAt,\n" +
                "       regionContent, email from TransactionHistory\n" +
                "inner join Goods G on TransactionHistory.goodsId = G.goodsId\n" +
                "left join (select * from PicturesGoods where isFirst = 'Y') PG on G.goodsId = PG.goodsId\n" +
                "inner join ChatRooms CR on G.goodsId = CR.goodsId\n" +
                "inner join User U on (CR.buyerId = U.userId or CR.sellerId = U.userId) and U.userId <> ?\n" +
                "where TransactionHistory.status <> 'deleted' and crId = ? and (TransactionHistory.buyerId = ? or TransactionHistory.sellerId = ?)";
        Object[] getDealInfoParams = new Object[]{ userId, crId, userId, userId };
        return this.jdbcTemplate.queryForObject(getDealInfoQuery,
                (rs, rowNum) -> new GetMakeDealRes(
                        rs.getInt("thId"),
                        rs.getInt("goodsId"),
                        rs.getString("goodsTitle"),
                        rs.getString("goodsImg"),
                        rs.getString("isFreeShipping"),
                        rs.getString("askPrice"),
                        rs.getString("meetAt"),
                        rs.getString("regionContent"),
                        rs.getString("email")
                )
                , getDealInfoParams);
    }

    public GetMakeDealRes getGoodsInfo(int userId, int crId) {
        String getGoodsInfoQuery = "select null as thId ,Goods.goodsId, title as goodsTitle, PG.imgUrl as goodsImg, isFreeShipping, price as askPrice,\n" +
                "       CURRENT_DATE as meetAt, '' as regionContent, email\n" +
                "from Goods\n" +
                "inner join ChatRooms CR on Goods.goodsId = CR.goodsId\n" +
                "left join (select * from PicturesGoods where isFirst = 'Y') PG on Goods.goodsId = PG.goodsId\n" +
                "inner join User U on (CR.buyerId = U.userId or CR.sellerId = U.userId) and U.userId <> ?\n" +
                "where crId = ?";
        Object[] getGoodsInfoParams = new Object[]{ userId, crId };
        return this.jdbcTemplate.queryForObject(getGoodsInfoQuery,
                (rs, rowNum) -> new GetMakeDealRes(
                        rs.getInt("thId"),
                        rs.getInt("goodsId"),
                        rs.getString("goodsTitle"),
                        rs.getString("goodsImg"),
                        rs.getString("isFreeShipping"),
                        rs.getString("askPrice"),
                        rs.getString("meetAt"),
                        rs.getString("regionContent"),
                        rs.getString("email")
                )
                , getGoodsInfoParams);
    }
}
