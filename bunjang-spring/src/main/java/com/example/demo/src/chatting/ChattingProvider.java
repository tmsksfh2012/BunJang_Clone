package com.example.demo.src.chatting;

import com.example.demo.config.BaseException;
import com.example.demo.src.chatting.model.*;
import com.example.demo.src.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ChattingProvider {

    private final ChattingDao chattingDao;
    private final UserProvider userProvider;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ChattingProvider(ChattingDao chattingDao, UserProvider userProvider) {
        this.chattingDao = chattingDao;
        this.userProvider = userProvider;
    }

    public int checkCrId(int crId) throws BaseException {
        try{
            return chattingDao.checkCrId(crId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkRoomByGoodsId(int buyerId, int sellerId, int goodsId) throws BaseException {
            try {
            return chattingDao.checkRoomByGoodsId(buyerId, sellerId, goodsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkFromToId(int crId, int fromId, int toId) throws BaseException {
        try{
            return chattingDao.checkFromToId(crId, fromId, toId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkFromToId(int crId, int userId) throws BaseException {
        try{
            return chattingDao.checkFromToId(crId, userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTHByCrId(int crId) throws BaseException {
        try {
            return chattingDao.checkTHByCrId(crId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetRoomsRes> getRooms(int userId, Integer type) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try {
            List<GetRoomsRes> getRoomsRes;

            if (type.equals(1)) {
                getRoomsRes = chattingDao.getRoomsForBuy(userId);
            } else if (type.equals(2)) {
                getRoomsRes = chattingDao.getRoomsForSell(userId);
            } else {
                getRoomsRes = chattingDao.getRooms(userId);
            }

            return getRoomsRes;
        }
        catch (Exception exception) {
            throw new BaseException(GET_FAIL_CHATROOM);
        }
    }

    @Transactional
    public GetRoomsInfoRes getRoomsInfoAndChats(int crId, int userId) throws BaseException {
        if (checkCrId(crId) != 1) {
            // 해당 채팅방이 없는 경우
            throw new BaseException(POST_CHATS_EMPTY_CR_ID);
        }

        if (userProvider.checkUserId(userId) != 1) {
            // 해당 유저가 가입한 경우가 아닐 경우
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (checkFromToId(crId, userId) != 1) {
            // 해당 채팅방에 로그인한 유저가 참여한 경우가 아닐 경우
            throw new BaseException(GET_USERS_NOT_MATCH_USER);
        }

        try {
            GetRoomsInfoReq getRoomsInfoReq = chattingDao.getRoomInfo(crId);
            List<GetChatsRes> chats = getChats(userId, crId);
            return new GetRoomsInfoRes(getRoomsInfoReq.getNickName(), getRoomsInfoReq.getGoodsImg(), getRoomsInfoReq.getGoodsPrice(),
                    getRoomsInfoReq.getGoodsTitle(),getRoomsInfoReq.getGoodsCanBungaePay(), chats);
        }
        catch (Exception exception) {
            throw new BaseException(GET_FAIL_CHATROOM);
        }
    }

    public List<GetChatsRes> getChats(int userId, int crId) throws BaseException {
        if (checkCrId(crId) != 1) {
            // 해당 채팅방이 없는 경우
            throw new BaseException(POST_CHATS_EMPTY_CR_ID);
        }

        if (userProvider.checkUserId(userId) != 1) {
            // 해당 유저가 가입한 경우가 아닐 경우
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (checkFromToId(crId, userId) != 1) {
            // 해당 채팅방에 로그인한 유저가 참여한 경우가 아닐 경우
            throw new BaseException(GET_USERS_NOT_MATCH_USER);
        }

        try {
            List<GetChatsRes> getChatsRes = chattingDao.getChats(crId);
            return getChatsRes;
        }
        catch (Exception exception) {
            throw new BaseException(GET_FAIL_CHATS);
        }
    }

    public GetRoomsIdxReq getIdx(int crId) throws BaseException {
        if (checkCrId(crId) != 1) {
            // 해당 채팅방이 없는 경우
            throw new BaseException(POST_CHATS_EMPTY_CR_ID);
        }

        try {
            GetRoomsIdxReq getRoomsIdxReq = chattingDao.getIdx(crId);
            return getRoomsIdxReq;
        }
        catch (Exception exception) {
            throw new BaseException(GET_FAIL_CHATROOM);
        }
    }

    public GetMakeDealRes getDealInfo(int userId, int crId) throws BaseException {
        if (checkCrId(crId) != 1) {
            // 해당 채팅방이 없는 경우
            throw new BaseException(POST_CHATS_EMPTY_CR_ID);
        }

        if (userProvider.checkUserId(userId) != 1) {
            // 해당 유저가 가입한 경우가 아닐 경우
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (checkFromToId(crId, userId) != 1) {
            // 해당 채팅방에 로그인한 유저가 참여한 경우가 아닐 경우
            throw new BaseException(GET_USERS_NOT_MATCH_USER);
        }

        try {
            GetMakeDealRes getMakeDealRes;
            if (chattingDao.checkDealExist(userId, crId) != 0) {
                getMakeDealRes = chattingDao.getDealInfo(userId, crId);
            } else {
                getMakeDealRes = chattingDao.getGoodsInfo(userId, crId);
            }
            return getMakeDealRes;
        }
        catch (Exception exception) {
            throw new BaseException(GET_FAIL_DEAL_INFO);
        }
    }
}
