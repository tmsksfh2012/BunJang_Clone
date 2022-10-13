package com.example.demo.src.reviews;

import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;
    private final ReviewsProvider reviewsProvider;
    private final JwtService jwtService;

    @Autowired
    public ReviewsController(ReviewsService reviewsService, ReviewsProvider reviewsProvider, JwtService jwtService) {
        this.reviewsService = reviewsService;
        this.reviewsProvider = reviewsProvider;
        this.jwtService = jwtService;
    }

    /**
     * 리뷰 생성 API
     * [POST] /reviews/:thId
     * @return BaseResponse<PostChatRes>
     */
//    @ResponseBody
//    @PostMapping("/{thId}")
//    public

}
