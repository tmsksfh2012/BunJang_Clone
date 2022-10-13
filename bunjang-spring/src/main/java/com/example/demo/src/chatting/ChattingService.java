package com.example.demo.src.chatting;

import com.example.demo.config.BaseException;
import com.example.demo.src.chatting.model.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.GetChatUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ChattingService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChattingDao chattingDao;
    private final ChattingProvider chattingProvider;
    private final UserProvider userProvider;

    @Autowired
    public ChattingService(ChattingDao chattingDao, ChattingProvider chattingProvider, UserProvider userProvider) {
        this.chattingDao = chattingDao;
        this.chattingProvider = chattingProvider;
        this.userProvider = userProvider;
    }

    @Transactional
    public PostChatRoomsRes createChatRoom(int userIdx, PostChatRoomsReq postChatRoomsReq) throws BaseException {
        if (userProvider.checkUserId(postChatRoomsReq.getBuyerId()) != 1 || userProvider.checkUserId(postChatRoomsReq.getSellerId()) != 1) {
            // 해당 채팅 참여자들이 회원가입한 멤버들 중 없는 경우
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (chattingProvider.checkRoomByGoodsId(postChatRoomsReq.getBuyerId(), postChatRoomsReq.getSellerId(), postChatRoomsReq.getGoodsId()) != 0) {
            // 이미 생성된 방이 있을 경우 새로 제작 불가 (삭제 이후 제작 가능), 추후 불러올 수 있게 수정 예정
            throw new BaseException(GET_ALREADY_EXIST_CHATROOM);
        }

        try {
            GetChatUserRes getChatUserRes; // 타쿼리문을 불러오기 위한 User Model 불러오기
            int crId = chattingDao.createChatRoom(postChatRoomsReq); // 채팅방 insert 이후 해당 채팅방 idx 불러오기

            if (userIdx == postChatRoomsReq.getBuyerId()) { // 상대 유저 정보 불러오기
                getChatUserRes = userProvider.getChatUserInfo(postChatRoomsReq.getGoodsId(), postChatRoomsReq.getSellerId());
                // 접속 유저가 buyer일 경우 seller의 정보를 불러오기
            } else {
                getChatUserRes = userProvider.getChatUserInfo(postChatRoomsReq.getGoodsId(), postChatRoomsReq.getBuyerId());
                // 접속 유저가 seller일 경우 buyer의 정보를 불러오기
            }

            return new PostChatRoomsRes(crId, postChatRoomsReq.getBuyerId(), postChatRoomsReq.getSellerId(),
                    postChatRoomsReq.getGoodsId(),
                    getChatUserRes.getNickName(), getChatUserRes.getUserImg(), getChatUserRes.getRating(),
                    getChatUserRes.getCountOfReview(),getChatUserRes.getTransactionHistory(),
                    getChatUserRes.getGoodsTitle(),getChatUserRes.getGoodsPrice(), getChatUserRes.getGoodsImg()
                    );
        } catch (Exception exception) {
            throw new BaseException(POST_FAIL_CHATROOM);
        }
    }

    public void deleteChatRoom(int crId, PatchChatRoomsReq patchChatRoomsReq) throws BaseException {
        if (chattingDao.checkCrId(crId, patchChatRoomsReq.getBuyerId(), patchChatRoomsReq.getSellerId()) == 0) {
            throw new BaseException(GET_FAIL_CHATROOM_SET);
        }

        try {
            int result = chattingDao.deleteChatRoom(crId, patchChatRoomsReq);

            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_CHATROOM);
            }
        } catch (BaseException exception) {
            throw new BaseException(MODIFY_FAIL_CHATROOM);
        }
    }

    public int alarmToggleChatRoom(int crId, PatchChatRoomsReq patchChatRoomsReq, boolean isUserBuyer) throws BaseException {
        if (chattingDao.checkCrId(crId, patchChatRoomsReq.getBuyerId(), patchChatRoomsReq.getSellerId()) == 0) {
            throw new BaseException(GET_FAIL_CHATROOM_SET);
        }

        try {
            if (isUserBuyer == true) {
                return chattingDao.alarmToggleChatRoomOnBuyer(crId);
            } else {
                return chattingDao.alarmToggleChatRoomOnSeller(crId);
            }
        } catch (Exception exception) {
            throw new BaseException(MODIFY_FAIL_CHATROOM);
        }
    }

    public PostChatRes createChat(PostChatReq postChatReq) throws BaseException {
        BaseExceptionInCreateChat(postChatReq);
        // 오류 상황들에 대해 따로 함수 extract
        try {
            int chatId = chattingDao.createChat(postChatReq);
            return new PostChatRes(chatId, postChatReq.getContent(), postChatReq.getFromId(), postChatReq.getType());
        } catch (Exception exception) {
            throw new BaseException(POST_ERROR_CHATS);
        }
    }

    private void BaseExceptionInCreateChat(PostChatReq postChatReq) throws BaseException {
        if (chattingProvider.checkCrId(postChatReq.getCrId()) != 1) {
            // 해당 채팅방이 없는 경우
            throw new BaseException(POST_CHATS_EMPTY_CR_ID);
        }

        if (userProvider.checkUserId(postChatReq.getFromId()) != 1 || userProvider.checkUserId(postChatReq.getToId()) != 1) {
            // 해당 채팅 참여자들이 회원가입한 멤버들 중 없는 경우
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (chattingProvider.checkFromToId(postChatReq.getCrId(), postChatReq.getFromId(), postChatReq.getToId()) != 1) {
            // 해당 채팅방의 참여자 idx가 일치하지 않는 경우
            throw new BaseException(GET_USERS_NOT_MATCH_USER);
        }
    }

    @Transactional
    public PostMakeDealRes makeDeal(int userId, int crId, PostMakeDealReq postMakeDealReq) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 해당 채팅 참여자들이 회원가입한 멤버들 중 없는 경우
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (chattingProvider.checkCrId(crId) != 1) {
            // 해당 채팅방이 없는 경우
            throw new BaseException(POST_CHATS_EMPTY_CR_ID);
        }

        if (chattingProvider.checkTHByCrId(crId) != 0) {
            // 이미 직거래 신청한 기록이 있을 경우
            throw new BaseException(POST_ALREADY_TRANSACTION_EXIST);
        }

        try {
            GetRoomsIdxReq getRoomsIdxReq = chattingProvider.getIdx(crId);
            PostMakeDealRes postMakeDealRes = chattingDao.makeDeal(getRoomsIdxReq, postMakeDealReq);
            return postMakeDealRes;
        } catch (Exception exception) {
            throw new BaseException(POST_ERROR_DEAL);
        }
    }

    @Transactional
    public PostMakeDealRes patchDeal(int userId, int crId, PostMakeDealReq postMakeDealReq) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 해당 채팅 참여자들이 회원가입한 멤버들 중 없는 경우
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (chattingProvider.checkCrId(crId) != 1) {
            // 해당 채팅방이 없는 경우
            throw new BaseException(POST_CHATS_EMPTY_CR_ID);
        }

        if (chattingProvider.checkTHByCrId(crId) != 1) {
            // 이미 직거래 신청한 기록이 없을 경우
            throw new BaseException(PATCH_TRANSACTION_NOT_EXIST);
        }

        try {
            PostMakeDealRes postMakeDealRes = chattingDao.patchDeal(crId, postMakeDealReq);
            return postMakeDealRes;
        } catch (Exception exception) {
            exception.printStackTrace();
//            throw null;
            throw new BaseException(PATCH_ERROR_DEAL);
        }
    }

    public PostMakeDealRes deleteDeal(int userId, int crId) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 해당 채팅 참여자들이 회원가입한 멤버들 중 없는 경우
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (chattingProvider.checkCrId(crId) != 1) {
            // 해당 채팅방이 없는 경우
            throw new BaseException(POST_CHATS_EMPTY_CR_ID);
        }

        if (chattingProvider.checkTHByCrId(crId) != 1) {
            // 이미 직거래 신청한 기록이 없을 경우
            throw new BaseException(PATCH_TRANSACTION_NOT_EXIST);
        }

        try {
            PostMakeDealRes postMakeDealRes = chattingDao.deleteDeal(crId);
            return postMakeDealRes;
        } catch (Exception exception) {
            throw new BaseException(PATCH_FAIL_DELETE_DEAL);
        }
    }

}
