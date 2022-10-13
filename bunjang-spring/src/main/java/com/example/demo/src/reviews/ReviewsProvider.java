package com.example.demo.src.reviews;

import com.example.demo.src.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewsProvider {

    private final ReviewsDao reviewsDao;
    private final UserProvider userProvider;

    @Autowired
    public ReviewsProvider(ReviewsDao reviewsDao, UserProvider userProvider) {
        this.reviewsDao = reviewsDao;
        this.userProvider = userProvider;
    }
}
