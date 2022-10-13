package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.search.model.GetSearchPopular;
import com.example.demo.src.search.model.GetSearchRecent;
import com.example.demo.src.user.model.PostLoginRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;

@RestController
@RequestMapping("/search")
public class SearchController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;
    @Autowired
    private final JwtService jwtService;

    public SearchController(SearchProvider searchProvider, SearchService searchService, JwtService jwtService){
        this.searchProvider = searchProvider;
        this.searchService = searchService;
        this.jwtService = jwtService;
    }

    /**
     * 인기 검색어 API
     * [GET] /search/popular
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @GetMapping("/popular")
    public BaseResponse<List<GetSearchPopular>> getPopular() {
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (searchProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetSearchPopular> res = searchProvider.getSearchPopular();

            if(res.isEmpty()) {
                return new BaseResponse<>(GET_SEARCH_LIST_NO_EXIST);
            }

            return new BaseResponse<>(res);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 최근 검색어 API
     * [GET] /search/popular
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @GetMapping("/recent")
    public BaseResponse<List<GetSearchRecent>> getRecent() {
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (searchProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetSearchRecent> res = searchProvider.getSearchRecent(userIdByJwt);

            if(res.isEmpty()) {
                return new BaseResponse<>(GET_SEARCH_LIST_NO_EXIST);
            }

            return new BaseResponse<>(res);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 최근 검색어 삭제 API
     * [PATCH] /search/:searchId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("{searchId}")
    public BaseResponse<String> deleteRecent(@PathVariable (required = false) String searchId) {
        if (searchId == null) {
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        if(!isRegexInteger(searchId)) {
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (searchProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if(searchProvider.checkSearchStatusBySearchIdUserId(searchId, userIdByJwt) == 0) {
                return new BaseResponse<>(GET_SEARCH_NOT_EXIST);
            }


            searchService.deleteRecent(searchId);

            return new BaseResponse<>("success");

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
