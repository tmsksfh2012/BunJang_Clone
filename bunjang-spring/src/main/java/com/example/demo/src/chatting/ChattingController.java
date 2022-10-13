package com.example.demo.src.chatting;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chatting.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexDate;

@RestController
@RequestMapping("/chat-rooms")
public class ChattingController {

    @Autowired
    private final ChattingService chattingService;
    @Autowired
    private final ChattingProvider chattingProvider;
    @Autowired
    private final JwtService jwtService;

    public ChattingController(ChattingService chattingService, ChattingProvider chattingProvider, JwtService jwtService) {
        this.chattingService = chattingService;
        this.chattingProvider = chattingProvider;
        this.jwtService = jwtService;
    }

    /**
     * 채팅 생성 API
     * [POST] /chat-rooms/:crId
     *
     * @return BaseResponse<PostChatRes>
     */
    @ResponseBody
    @PostMapping("/{crId}")
    public BaseResponse<PostChatRes> createChat(@PathVariable("crId") int crId, @RequestBody PostChatReq postChatReq) {
        try {
            int userIdByJwt = jwtService.getUserId();

            //userId와 접근한 유저가 같은지 확인
            if (postChatReq.getFromId() != userIdByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 채팅 유형을 따로 안 정했으면 기본적으로 일반 comment 유형으로 세팅
            if (postChatReq.getType() == null) {
                postChatReq.setType("letter");
            }
            // PathVariable로 주어진 crId 세팅하기
            postChatReq.setCrId(crId);

            if (postChatReq.getContent() == null || postChatReq.getContent().trim().isEmpty()) {
                // 해당 메시지가 null값이거나 빈 값인 경우
                throw new BaseException(POST_CHATS_EMPTY_CONTENT);
            }

            PostChatRes postChatRes = chattingService.createChat(postChatReq);
            return new BaseResponse<>(postChatRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채팅방 생성 API
     * [POST] /chat-rooms
     *
     * @return BaseResponse<PostChatRoomsRes>
     */
    @ResponseBody
    @PostMapping
    public BaseResponse<PostChatRoomsRes> createChatRoom(@RequestBody PostChatRoomsReq postChatRoomsReq) {
        try {
            int userIdByJwt = jwtService.getUserId();

            //userId와 접근한 유저가 같은지 확인
            if (!(postChatRoomsReq.getSellerId() == userIdByJwt || postChatRoomsReq.getBuyerId() == userIdByJwt)) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostChatRoomsRes postChatRoomsRes = chattingService.createChatRoom(userIdByJwt, postChatRoomsReq);
            return new BaseResponse<>(postChatRoomsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채팅방 삭제 API
     * [PATCH] /chat-rooms
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{crId}")
    public BaseResponse<String> changeStatusChatRoom(@PathVariable("crId") int crId, @RequestBody PatchChatRoomsReq patchChatRoomsReq) {
        try {
            int userIdByJwt = jwtService.getUserId();

            //userId와 접근한 유저가 같은지 확인
            if (!(patchChatRoomsReq.getBuyerId() == userIdByJwt || patchChatRoomsReq.getSellerId() == userIdByJwt)) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            patchChatRoomsReq.setStatus("deleted");
            chattingService.deleteChatRoom(crId, patchChatRoomsReq);

            String result = "ChatRoom quited!";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 채팅방 알람설정 API
     * [PATCH] /chat-rooms/alarms/:crId
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/alarms/{crId}")
    public BaseResponse<String> deleteChatRoom(@PathVariable("crId") int crId, @RequestBody PatchChatRoomsReq patchChatRoomsReq) {
        //jwt에서 id 추출.
        try {
            int userIdByJwt = jwtService.getUserId();
            boolean isUserBuyer;
            String user;
            //userId와 접근한 유저가 같은지 확인
            if (patchChatRoomsReq.getBuyerId() == userIdByJwt) {
                isUserBuyer = true;
                user = "Buyer ";
            } else if (patchChatRoomsReq.getSellerId() == userIdByJwt) {
                isUserBuyer = false;
                user = "Seller ";
            } else {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            String result = "";
            int set = chattingService.alarmToggleChatRoom(crId, patchChatRoomsReq, isUserBuyer);

            if (set == 0) {
                result = "Alarm off!";
            } else {
                result = "Alarm On!";
            }
            return new BaseResponse<>(user + result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그인한 유저 채팅방들 가져오기 API
     * [GET] /chat-rooms
     *
     * @return BaseResponse<List < GetRoomsRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetRoomsRes>> getRooms(@RequestParam(required = false) Integer type) {
        try {
            int userId = jwtService.getUserId();

            // 입력 없을 시 전체 대화 가져오기.  (0: 전체대화, 1: 구매대화, 2: 판매대화)
            if (type == null) {
                type = 0;
            }

            List<GetRoomsRes> getRoomsRes = chattingProvider.getRooms(userId, type);

            return new BaseResponse<>(getRoomsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채팅방 내부 정보 및 채팅 세부 내용 가져오기 API
     * [GET] /chat-rooms/:crId
     *
     * @return BaseResponse<GetRoomsInfoRes>
     */
    @ResponseBody
    @GetMapping("/{crId}")
    public BaseResponse<GetRoomsInfoRes> getRoomsInfoAndChats(@PathVariable("crId") int crId) {
        try {
            Integer userId = jwtService.getUserId();

            GetRoomsInfoRes getRoomsInfoRes = chattingProvider.getRoomsInfoAndChats(crId, userId);

            return new BaseResponse<>(getRoomsInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 해당 채팅방 내 채팅 세부 내용만 가져오기 API
     * [GET] /chat-rooms/chats/:crId
     *
     * @return BaseResponse<List < GetChatsRes>>
     */
    @ResponseBody
    @GetMapping("/chats/{crId}")
    public BaseResponse<List<GetChatsRes>> getChats(@PathVariable("crId") int crId) {
        try {
            Integer userId = jwtService.getUserId();

            List<GetChatsRes> getChatsRes = chattingProvider.getChats(userId, crId);

            return new BaseResponse<>(getChatsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 해당 채팅방 직거래 약속 잡기 API
     * [POST] /chat-rooms/direct-deal/:crId
     * @return BaseResponse<PostMakeDealRes>
     */
    @ResponseBody
    @PostMapping("/direct-deal/{crId}")
    public BaseResponse<PostMakeDealRes> makeDeal(@PathVariable("crId") int crId, @RequestBody PostMakeDealReq postMakeDealReq) {
        try {

            makeDealValidation(postMakeDealReq);

            Integer userId = jwtService.getUserId();
            PostMakeDealRes postMakeDealRes = chattingService.makeDeal(userId, crId, postMakeDealReq);
            return new BaseResponse<>(postMakeDealRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 해당 채팅방 직거래 약속 수정하기 API
     * [PATCH] /chat-rooms/direct-deal/:thId
     * @return BaseResponse<PostMakeDealRes>
     */
    @ResponseBody
    @PatchMapping("/direct-deal/{crId}")
    public BaseResponse<PostMakeDealRes> patchDeal(@PathVariable("crId") int crId, @RequestBody PostMakeDealReq postMakeDealReq) {
        try {
            makeDealValidation(postMakeDealReq);

            Integer userId = jwtService.getUserId();
            PostMakeDealRes postMakeDealRes = chattingService.patchDeal(userId, crId, postMakeDealReq);
            return new BaseResponse<>(postMakeDealRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 해당 채팅방 직거래 약속 취소하기 API
     * [PATCH] /chat-rooms/direct-deal-removal/:thId
     * @return BaseResponse<PostMakeDealRes>
     */
    @ResponseBody
    @PatchMapping("/direct-deal-removal/{crId}")
    public BaseResponse<PostMakeDealRes> deleteDeal(@PathVariable("crId") int crId) {
        try {
            Integer userId = jwtService.getUserId();
            PostMakeDealRes postMakeDealRes = chattingService.deleteDeal(userId, crId);
            return new BaseResponse<>(postMakeDealRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 해당 채팅방 직거래 약속 상세 조회하기 API
     * [PATCH] /chat-rooms/direct-deal-removal/:thId
     * @return BaseResponse<PostMakeDealRes>
     */
    @ResponseBody
    @GetMapping("/direct-deal/{crId}")
    public BaseResponse<GetMakeDealRes> getDealInfo(@PathVariable("crId") int crId) {
        try {
            Integer userId = jwtService.getUserId();
            GetMakeDealRes getMakeDealRes = chattingProvider.getDealInfo(userId, crId);
            return new BaseResponse<>(getMakeDealRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 해당 채팅방 직거래 약속 API 관련 validation
     * 형식적 validation 처리
     */
    private void makeDealValidation(PostMakeDealReq postMakeDealReq) throws BaseException {
        if (postMakeDealReq.getRegionContent() == null || postMakeDealReq.getRegionContent().trim().isEmpty()) {
            // 해당 만날 장소 값이 null값이거나 빈 값인 경우
            throw new BaseException(POST_REGION_EMPTY_CONTENT);
        }

        if (postMakeDealReq.getMeetAt() == null || postMakeDealReq.getMeetAt().trim().isEmpty()) {
            // 해당 거래일이 null값이거나 빈 값인 경우
            throw new BaseException(POST_DATE_EMPTY_CONTENT);
        }

        if (!(isRegexDate(postMakeDealReq.getMeetAt()))) {
            throw new BaseException(POST_DATE_WRONG_CONTENT);
        }

        if (postMakeDealReq.getAskPrice() == null) {
            // 해당 거래일이 null값일 경우
            throw new BaseException(POST_PRICE_EMPTY_CONTENT);
        }
    }
}
