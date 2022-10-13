package com.example.demo.src.following;

import com.example.demo.config.BaseException;
import com.example.demo.src.following.model.FollowingGoods;
import com.example.demo.src.following.model.GetFollowerRes;
import com.example.demo.src.following.model.GetFollowingRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FollowingProvider {
    private final FollowingDao followingDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FollowingProvider(FollowingDao followingDao, JwtService jwtService) {
        this.followingDao = followingDao;
        this.jwtService = jwtService;
    }

    public int checkUserStatusByUserId(String userId) throws BaseException {
        try {
            return followingDao.checkUserStatusByUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkFollowingStatusByUserId(String myId, String userId) throws BaseException {
        try {
            return followingDao.checkFollowingStatusByUserId(myId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowingRes> getFolloweeRes(String userId) throws BaseException {
        try {
            return followingDao.getFolloweeRes(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowerRes> getFollowerRes(String userId) throws BaseException {
        try {
            return followingDao.getFollowerRes(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<FollowingGoods> getFolloweeGoods(String userId) throws BaseException {
        try {
            return followingDao.getFolloweeGoods(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
