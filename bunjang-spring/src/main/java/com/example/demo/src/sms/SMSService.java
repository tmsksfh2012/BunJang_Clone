package com.example.demo.src.sms;

import com.example.demo.config.BaseException;
import com.example.demo.src.sms.model.MessageReq;
import com.example.demo.src.sms.model.SMSReq;
import com.example.demo.src.sms.model.PostSMSRes;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SMSService {

    private final SMSDao smsDao;
    private final JwtService jwtService;

    @Autowired
    public SMSService(SMSDao smsDao,  JwtService jwtService) {
        this.smsDao = smsDao;
        this.jwtService = jwtService;

    }

    public void postSMS(String userId, String phoneNumber, String content) throws JsonProcessingException,
            URISyntaxException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String serviceId = "ncp:sms:kr:269930766223:bunjang_project";
        String accessKey = "AUYicG3wKNmoeVtYEk1J";

        Long time = System.currentTimeMillis();
        List<MessageReq> messages = new ArrayList<>();
        messages.add(new MessageReq(phoneNumber, content));

        System.out.println(content);

        SMSReq smsRequest = new SMSReq("SMS", "COMM",
                "82", "01084355650", "[인증번호]\n인증 번호를 입력해주세요.\n\n" + content,
                messages);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(smsRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        String sig = makeSignature(time); //암호화
        headers.set("x-ncp-apigw-signature-v2", sig);

        HttpEntity<String> body = new HttpEntity<>(jsonBody,headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        PostSMSRes smsResponse = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+serviceId+"/messages"), body, PostSMSRes.class);

        smsDao.postSMS(userId, content);

    }

    public String makeSignature(Long time) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String serviceId = "ncp:sms:kr:269930766223:bunjang_project";
        String url = "/sms/v2/services/" + serviceId + "/messages";
        String accessKey = "AUYicG3wKNmoeVtYEk1J";
        String secretKey = "5MvvY3LBbvDltZ4659O5CfQrvQSeS45L3b9AlvFu";

        System.out.println("타임스탬프: " + time);

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(time)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

        return encodeBase64String;
    }

    public void patchSMS(String userId) throws BaseException{
        try{
            smsDao.patchSMS(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}