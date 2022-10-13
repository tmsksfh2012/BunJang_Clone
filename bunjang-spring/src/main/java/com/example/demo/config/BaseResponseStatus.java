package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    EMPTY_PATH_VARIABLE(false, 2004, "PathVariable를 입력해주세요."),
    INVALID_ADDRESS(false, 2005, "올바르지 않은 주소입니다."),
    INVAILD_PATH_VARIABLE(false, 2006, "올바르지 않은 PathVariable 형식입니다."),
    INVALID_QUERY_PARAMS(false, 2007, "올바르지 않은 Query Params 형식입니다."),
    EMPTY_QUERY_PARAMS(false, 2008, "Query Params를 입력해주세요."),
    EMPTY_BODY(false, 2009, "body를 입력해주세요."),
    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    USERS_TOKEN_EMPTY(false, 2011, "token을 입력해주세요"),
    USERS_TYPE_EMPTY(false, 2012, "type을 입력해주세요"),
    USERS_TYPE_ERROR_TYPE(false, 2013, "type이 올바르지 않습니다"),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2014, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2015, "이메일을 올바르게 입력해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2016,"이미 가입된 이메일 주소입니다. 다른 이메일을 입력하여 주세요."),
    USERS_NOT_EXISTS(false, 2017, "존재하지 않는 유저입니다."),
    CAN_NOT_BAN_MYSELF(false, 2018, "당신은 소중합니다."),
    EXIST_BAN_USER(false, 2344, "이미 차단 되어 있는 유저입니다."),

    NO_EXIST_BAN_USER(false, 2345, "유저에게 해당 벤 유저가 없습니다."),

    POST_USERS_PASSWORD_COMBINATION(false, 2019, "영문/숫자/특수문자 2가지 이상 조합(8~20자)"),
    POST_USERS_PASSWORD_CONTINUOUS_OR_SAME_WORD(false, 2020, "3개 이상 연속되거나 동일한 문자/숫자 제외"),
    POST_USERS_PASSWORD_CONTAIN_ID(false, 2021, "아이디(이메일 제외)"),
    POST_USERS_EMPTY_NAME(false, 2022, "이름을 입력해주세요."),
    POST_USERS_INVALID_NAME(false, 2023, "이름을 정확히 입력하세요."),
    POST_USERS_EMPTY_PHONE(false, 2024, "휴대폰 번호를 입력해주세요."),
    POST_USERS_INVALID_PHONE(false, 2025, "휴대폰 번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONE(false, 2026, "가입된 휴대폰 번호입니다."),

    NO_EXIST_USER_ID(false, 2100, "user id를 입력해주세요."),

    NICKNAME_CAN_NOT_EMPTY(false, 2900, "닉네임을 입력해주세요."),

    // [POST] /goods
    POST_GOODS_EMPTY_TITLE(false, 2027, "제목을 입력해주세요."),
    POST_GOODS_EMPTY_CONTENT(false, 2028, "상품 설명을 10글자 이상 입력해주세요."),
    POST_GOODS_INVALID_PRICE(false, 2029, "100원이상 입력해주세요."),
    POST_GOODS_EMPTY_CATEGORY(false, 2030, "카테고리를 선택해주세요."),
    POST_GOODS_EMPTY_TAG(false, 2031, "태그를 선택해주세요."),
    POST_GOODS_EMPTY_IMGS(false, 2032, "상품 사진을 등록해주세요."),

    // [PATCH] /goods
    PATCH_GOODS_EMPTY_NUMBER_OF_GOODS(false, 2033, "상품 개수를 입력해주세요."),

    // [POST] /wish-list
    NO_EXIST_GOODS_ID(false, 2034, "상품 아이디를 입력해주세요."),
    CAN_NOT_LIKE_YOUR_GOODS(false, 2035, "본인 상품을 찜할 수 없습니다."),

    POST_ALREADY_EXIST_WISH(false, 2036, "이미 관심 목록에 존재하는 상품입니다."),
    POST_NO_EXIST_WISH(false, 2037, "관심 목록에 없는 상품입니다."),

    // [GET] /search
    GET_SEARCH_NOT_EXIST(false, 2040, "유저에게 검색어가 존재하지 않습니다."),

    // [POST] /following
    INVALID_BODY(false, 2041, "올바르지 않은 body 형식입니다."),

    POST_ALREADY_EXIST_FOLLOWING(false, 2042, "이미 팔로우한 유저입니다."),
    POST_NO_EXIST_FOLLOWING(false, 2043, "팔로우하지 않은 유저입니다."),

    // [GET] /chat-rooms
    GET_FAIL_CHATROOM_SET(false, 2101, "해당 설정값에 따른 채팅방이 존재하지 않습니다."),
    GET_ALREADY_EXIST_CHATROOM(false, 2102, "해당 채팅방이 이미 존재합니다."),
    GET_USERS_NOT_MATCH_USER(false, 2112, "채팅방 내 참여유저 목록이 일치하지 않습니다."),
    POST_FAIL_CHATROOM(false, 2113, "해당 채팅방을 만드는데 오류가 있습니다."),

    // [POST] /chats
    POST_CHATS_EMPTY_CR_ID(false, 2130, "해당 채팅방이 존재하지 않습니다."),
    POST_CHATS_EMPTY_CONTENT(false, 2131, "전달할 내용을 입력해주세요."),
    POST_REGION_EMPTY_CONTENT(false, 2132, "(거래 장소) 항목을 입력해주세요."),
    POST_DATE_EMPTY_CONTENT(false, 2133, "(날짜 입력) 항목을 입력해주세요."),
    POST_DATE_WRONG_CONTENT(false, 2134, "(날짜 입력) 형식을 확인해주세요."),
    POST_PRICE_EMPTY_CONTENT(false, 2135, "(판매금액) 항목을 입력해주세요."),
    POST_ALREADY_TRANSACTION_EXIST(false, 2141, "이미 거래 중인 내역이 있습니다."),
    PATCH_TRANSACTION_NOT_EXIST(false, 2142, "현재 거래 중인 내역이 없습니다."),

    // [GET] /regions
    GET_REGION_EXIST_ERROR(false, 2151, "해당 idx에 해당되는 지역 값이 없습니다."),
    GET_REGION_LONGITUDE_ERROR(false, 2152, "해당 입력 값이 경도로 호환되지 않습니다."),
    GET_REGION_LATITUDE_ERROR(false, 2153, "해당 입력 값이 위도로 호환되지 않습니다."),

    // [GET] /reviews
    GET_TRANSACTION_HISTORY_NULL(false, 2170, "거래 이력이 존재하지 않습니다."),
    GET_STAR_RATING_ERROR(false, 2171, "별점 입력은 0.0 ~ 5.0까지 0.5 단위로 가능합니다."),
    GET_CONTENT_BLANK(false, 2172, "공백 없이 평점 후기를 입력해주세요."),
    GET_CONTENT_LESS_THAN_10(false, 2173, "평점 후기는 10글자 이상 적어주세요."),
    GET_TRANSACTION_PARTICIPANT_ERROR(false, 2174, "로그인 유저가 해당 거래 이력과 관련이 없습니다."),
    GET_ALREADY_REVIEWS_ON_USERID(false, 2175, "이미 입력한 거래 후기가 있습니다."),
    GET_NON_REVIEWS_BUYER(false, 2176, "입력된 후기가 존재하지 않아 후기에 대한 답글을 달을 수가 없습니다."),

    // [PATCH] /reviews
    PATCH_NON_REVIEWS_ON_USER_ID(false, 2181, "입력된 후기가 존재하지 않거나 이미 삭제되었습니다."),
    PATCH_NON_PATH_VARIABLE(false, 2181, "삭제하고자하는 리뷰로 선택한 값이 없습니다."),

    // [POST] /user-regions
    POST_ALREADY_USER_REGIONS(false, 2191, "이미 해당 지역은 유저 지역으로 등록된 지역입니다."),

    // [PATCH] /user-regions
    PATCH_NON_USER_REGIONS(false, 2192, "유저 지역 내에 해당 지역 값이 없습니다."),
    PATCH_USER_REGIONS_RANGE_ERROR(false, 2193, "유저 동네 범위 설정이 잘못되었습니다. 0 ~ 3 중 선택 가능합니다."),

    // /delivery-address
    GET_LOST_OF_DELIVERY_ON_USER(false, 2200, "주소지는 최대 5개까지 관리할 수 있습니다."),
    GET_NON_ADDRESS_ID(false, 2201, "해당 address id가 존재하지 않습니다."),

    // account
    GET_NON_ACCOUNT_ID(false, 2202, "해당 account id가 존재하지 않습니다."),
    GET_LOST_OF_ACCOUNT_ON_USER(false, 2203, "계좌는 최대 2개까지 관리할 수 있습니다."),
    POST_USERS_NON_BANK_ID(false, 2327, "해당 bankId는 존재하지 않습니다."),
    POST_CANNOT_EMPTY_VALUE(false, 2999, "빈 값은 입력할 수 없습니다."),

    //[POST] /sms
    INVALID_VERIFY(false, 2910, "인증 번호가 틀렸습니다."),




    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    POST_USERS_EXIST_SOCIAL(false, 3010, "소셜회원은 소셜로그인을 이용해주세요"),
    USERS_LOGIN_NOT_MATCH(false, 3012, "아이디 또는 비밀번호가 일치하지 않습니다"),
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    DELETED_USER(false, 3015, "탈퇴한 유저입니다."),
    INVALID_ADDRESS_ID(false, 3016, "올바르지 않은 addressId 입니다."),
    INVALID_JWT_TOKEN_USER(false, 3101, "jwt 토큰에 해당하는 유저가 없습니다."),

    // [GET] /goods
    GET_GOODS_LIST_NO_EXIST(true, 3017, "상품 리스트가 존재하지 않습니다."),
    GET_GOODS_NOT_EXIST(true, 3018, "상품이 존재하지 않습니다."),
    NO_EXIST_KEYWORD(false, 3020, "검색어가 존재하지 않습니다."),
    GET_GOODS_FILTER_ERROR(false, 3021, "필터 조회에 실패하였습니다."),
    NO_EXIST_ORDER_ID(false, 3022, "정렬 번호가 존재하지 않습니다."),
    NO_EXIST_CATEGORY_ID(false, 3023, "카테고리 아이디가 존재하지 않습니다."),
    NO_EXIST_BRAND_ID(false, 3024, "브랜드 아이디가 존재하지 않습니다."),
    NO_EXIST_KEYWORD_ID(false, 3025, "키워드가 존재하지 않습니다."),

    NO_EXIST_CATEGORY_LIST(true, 3333, "카테고리 리스트가 존재하지 않습니다."),
    NO_EXIST_BRAND_LIST(true, 3334, "브랜드 리스트가 존재하지 않습니다."),

    // [GET] /following
    GET_FOLLOWING_LIST_NO_EXIST(true, 3026, "팔로우 목록이 존재하지 않습니다."),

    // [GET] /search

    GET_SEARCH_LIST_NO_EXIST(true, 3027, "키워드 목록이 존재하지 않습니다."),

    // address
    POST_ERROR_ADDRESS_ID(false, 3112, "배송지를 추가하는데 실패했습니다."),
    PATCH_ERROR_ADDRESS_ID(false, 3113, "배송지를 수정하는데 실패했습니다."),
    GET_ERROR_ADDRESS_ID(false, 3114, "배송지를 불러오는데 실패했습니다."),

    // account
    POST_ERROR_ACCOUNT_ID(false, 3122, "계좌를 추가하는데 실패했습니다."),
    PATCH_ERROR_ACCOUNT_ID(false, 3123, "계좌를 수정하는데 실패했습니다."),
    GET_ERROR_ACCOUNT_ID(false, 3124, "계좌를 불러오는데 실패했습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    KAKAO_LOGIN_FAIL(false,4004,"카카오 로그인에 실패했습니다"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_GOODS(false,4015,"상품 수정 실패"),

    //[GET] /chatRoom
    GET_FAIL_CHATS(false, 4101, "해당 채팅 내역이 존재하지 않습니다."),
    GET_FAIL_CHATROOM(false, 4111, "채팅방 불러오기 작업 실패"),
    GET_FAIL_DEAL_INFO(false,4112, "해당 거래 내용 불러오기 실패"),

    //[PATCH] /chatRoom/{crIdx}
    MODIFY_FAIL_CHATROOM(false, 4113, "채팅방 수정작업 실패"),

    //[POST] /chats
    POST_ERROR_CHATS(false, 4121, "채팅 생성 실패"),

    //[POST] /direct-deal
    POST_ERROR_DEAL(false, 4131, "직거래 생성에 실패했습니다."),

    //[PATCH] /direct-deal
    PATCH_ERROR_DEAL(false, 4141, "직거래 수정에 실패했습니다."),
    PATCH_FAIL_DELETE_DEAL(false, 4142, "직거래 취소에 실패했습니다."),

    //[GET] /regions
    GET_REGIONS_FAIL(false, 4201, "주소를 불러오는데 실패했습니다."),

    //[POST] /reviews
    POST_ERROR_REVIEWS(false, 4301, "리뷰를 입력하는데 실패했습니다."),
    PATCH_ERROR_REVIEWS(false, 4302, "리뷰를 삭제하는데 실패했습니다."),

    //[GET] /reviews
    GET_ERROR_REVIEWS(false, 4303, "입력된 리뷰를 불러오는데 실패했습니다."),

    //[POST] /user-regions
    POST_ERROR_USER_REGIONS(false, 4401, "유저 지역을 추가하는데 실패했습니다."),
    //[PATCH] /user-regions
    PATCH_ERROR_USER_REGIONS(false, 4402, "유저 대표 지역을 변경하는데 실패했습니다."),
    PATCH_ERROR_USER_REGIONS_RANGE(false, 4403, "유저 지역 범위 설정을 변경하는데 실패했습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}