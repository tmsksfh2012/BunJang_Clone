package com.example.demo.src.following;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FollowingService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowingDao followingDao;
    private final FollowingProvider followingProvider;
    private final JwtService jwtService;


    @Autowired
    public FollowingService(FollowingDao followingDao, FollowingProvider followingProvider, JwtService jwtService) {
        this.followingDao = followingDao;
        this.followingProvider = followingProvider;
        this.jwtService = jwtService;
    }

    public void postFollowing(String myId, String userId) throws BaseException {
        try {
            this.followingDao.postFollowing(myId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteFollowing(String myId, String userId) throws BaseException {
        try {
            this.followingDao.deleteFollowing(myId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
