package com.example.demo.src.keyword;

import com.example.demo.config.BaseException;
import com.example.demo.src.keyword.model.GetKeywordRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class KeywordProvider {
    private final KeywordDao keywordDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public KeywordProvider(KeywordDao keywordDao, JwtService jwtService) {
        this.keywordDao = keywordDao;
        this.jwtService = jwtService;
    }

    public int checkUserStatusByUserId(String userId) throws BaseException {
        try {
            return keywordDao.checkUserStatusByUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkKeywordByUserId(String userId, String keywordId) throws BaseException {
        try {
            return keywordDao.checkKeywordByUserId(userId, keywordId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetKeywordRes> getKeyword(String userId) throws BaseException {
        try {
            return keywordDao.getKeyword(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
