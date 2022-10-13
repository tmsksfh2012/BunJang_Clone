package com.example.demo.src.reviews;

import com.example.demo.src.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewsService {

    private final ReviewsProvider reviewsProvider;
    private final ReviewsDao reviewsDao;
    private final UserProvider userProvider;

    @Autowired
    public ReviewsService(ReviewsProvider reviewsProvider, ReviewsDao reviewsDao, UserProvider userProvider) {
        this.reviewsProvider = reviewsProvider;
        this.reviewsDao = reviewsDao;
        this.userProvider = userProvider;
    }
}
