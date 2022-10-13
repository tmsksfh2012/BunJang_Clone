package com.example.demo.src.keyword;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.keyword.model.GetKeywordRes;
import com.example.demo.src.keyword.model.PatchKeywordReq;
import com.example.demo.src.keyword.model.PostKeywordReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;

@RestController
@RequestMapping("/keywords")
public class KeywordController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final KeywordProvider keywordProvider;
    @Autowired
    private final KeywordService keywordService;
    @Autowired
    private final JwtService jwtService;

    public KeywordController(KeywordProvider keywordProvider, KeywordService keywordService, JwtService jwtService){
        this.keywordProvider = keywordProvider;
        this.keywordService = keywordService;
        this.jwtService = jwtService;
    }

    /**
     * 키워드 생성 API
     * [POST] /keywords
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postKeyword(@RequestBody PostKeywordReq postKeywordReq) {
        if(postKeywordReq == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (keywordProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            keywordService.postKeyword(userIdByJwt, postKeywordReq.getKeyword());

            return new BaseResponse<>("success");

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 키워드 삭제 API
     * [PATCH] /keywords
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> deleteKeyword(@RequestBody PatchKeywordReq patchKeywordReq) {
        if(patchKeywordReq == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        if(!isRegexInteger(patchKeywordReq.getKeywordId())) {
            return new BaseResponse<>(INVALID_BODY);
        }
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (keywordProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if(keywordProvider.checkKeywordByUserId(userIdByJwt,patchKeywordReq.getKeywordId()) == 0) {
                return new BaseResponse<>(NO_EXIST_KEYWORD_ID);
            }

            keywordService.deleteKeyword(userIdByJwt, patchKeywordReq.getKeywordId());

            return new BaseResponse<>("success");

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 키워드 조회 API
     * [PATCH] /keywords
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetKeywordRes>> getKeyword() {
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (keywordProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetKeywordRes> getKeywordResList =
                    keywordProvider.getKeyword(userIdByJwt);

            if(getKeywordResList.isEmpty()) {
                return new BaseResponse<>(NO_EXIST_KEYWORD_ID);
            }

            return new BaseResponse<>(getKeywordResList);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
