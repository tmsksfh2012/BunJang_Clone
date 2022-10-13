package com.example.demo.src.sms;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.sms.model.PostSMSCheckReq;
import com.example.demo.src.sms.model.PostSMSReq;
import com.example.demo.src.sms.model.PostSMSRes;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;
import static com.example.demo.utils.ValidationRegex.isRegexPhone;

@RestController
@RequestMapping("/sms")
public class SMSController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final SMSService smsService;
    @Autowired
    private final SMSProvider smsProvider;

    @Autowired
    private final JwtService jwtService;

    public SMSController(SMSService smsService, SMSProvider smsProvider, JwtService jwtService){
        this.smsService = smsService;
        this.smsProvider = smsProvider;
        this.jwtService = jwtService;
    }

    /**
     * 문자 발송
     * [POST] /sms
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postSMS(@RequestBody PostSMSReq postSMSReq) {
        if(postSMSReq == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        if (!isRegexPhone(postSMSReq.getPhoneNumber())) {
            return new BaseResponse<>(INVALID_BODY);
        }
        try {
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (smsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            Random random = new Random();        //랜덤 함수 선언
            int createNum = 0;            //1자리 난수
            String ranNum = "";            //1자리 난수 형변환 변수
            int letter = 6;            //난수 자릿수:6
            String resultNum = "";        //결과 난수

            for (int i = 0; i < letter; i++) {

                createNum = random.nextInt(9);        //0부터 9까지 올 수 있는 1자리 난수 생성
                ranNum = Integer.toString(createNum);  //1자리 난수를 String으로 형변환
                resultNum += ranNum;            //생성된 난수(문자열)을 원하는 수(letter)만큼 더하며 나열
            }

            smsService.postSMS(userIdByJwt, postSMSReq.getPhoneNumber(), resultNum);

            return new BaseResponse<>("success");

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));

        } catch (UnsupportedEncodingException | URISyntaxException | NoSuchAlgorithmException | InvalidKeyException |
                 JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 문자 발송 callback
     * [POST] /sms/check
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PostMapping("/check")
    public BaseResponse<String> postCheckSMS(@RequestBody PostSMSCheckReq postSMSCheckReq) {
        if(postSMSCheckReq == null) {
            return new BaseResponse<>(EMPTY_BODY);
        }
        try{
            // jwt 에서 userId 추출.
            String userIdByJwt = String.valueOf(jwtService.getUserId());
            if (smsProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            if(smsProvider.checkSMSStatus(userIdByJwt, postSMSCheckReq.getContent()) == 0) {
                return new BaseResponse<>(INVALID_VERIFY);
            }
            smsService.patchSMS(userIdByJwt);

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
