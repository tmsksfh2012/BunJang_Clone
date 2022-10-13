package com.example.demo.src.keyword;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class KeywordService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KeywordDao keywordDao;
    private final KeywordProvider keywordProvider;
    private final JwtService jwtService;


    @Autowired
    public KeywordService(KeywordDao keywordDao, KeywordProvider keywordProvider, JwtService jwtService) {
        this.keywordDao = keywordDao;
        this.keywordProvider = keywordProvider;
        this.jwtService = jwtService;

    }

    public void postKeyword(String myId, String keyword) throws BaseException {
        try {
            this.keywordDao.postKeyword(myId, keyword);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteKeyword(String myId, String keywordId) throws BaseException {
        try {
            this.keywordDao.deleteKeyword(myId, keywordId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
