package com.example.demo.src.sms;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SMSProvider {
    private final SMSDao smsDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SMSProvider(SMSDao smsDao, JwtService jwtService) {
        this.smsDao = smsDao;
        this.jwtService = jwtService;
    }

    public int checkUserStatusByUserId(String userId) throws BaseException {
        try {
            return smsDao.checkUserStatusByUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkSMSStatus(String userId, String content) throws BaseException{
        System.out.println("check");
        try{
            System.out.println(userId);
            System.out.println(content);
            return smsDao.postCheckSMS(userId, content);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
