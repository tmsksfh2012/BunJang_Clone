package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }
    //카카오 로그인
    //카카오 로그인은 토큰값과 닉네임(상점명) 보유
    public PostRegisterRes createKakaoUser(PostRegisterReq postRegisterReq) throws BaseException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String email = "";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + postRegisterReq.getToken());

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            int id = element.getAsJsonObject().get("id").getAsInt();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            if(hasEmail){
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            br.close();

        } catch (IOException e) {
            throw new BaseException(KAKAO_LOGIN_FAIL);
        }
        //이메일 중복체크
        int userIdx = 0;

        try{
            if(userProvider.checkEmail(email) == 1) { // 계정 중복
                int err = 1/0;
//          createUserDefault(userIdx);
            }
            else {
                System.out.println(email);
                System.out.println(postRegisterReq.getNickName());
                UserKakao user = new UserKakao(email, postRegisterReq.getNickName());
                System.out.println("여기");
                System.out.println(user.getEmail());
                System.out.println(user.getNickName());
                userIdx = userDao.createKakaoUser(user);
            }
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostRegisterRes(userIdx, jwt);

        } catch(ArithmeticException e) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //카카오 로그인
    //카카오 로그인은 토큰값과 닉네임(상점명) 보유
    public PostLoginRes loginKakaoUser(PostLoginReq postLoginReq) throws BaseException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String email = "";

        //access_token을 이용하여 사용자 정보 조회
        try {
            System.out.println("로그인 연결");
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + postLoginReq.getToken());

            System.out.println("토큰 가져오기 성공");

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            System.out.println("response 가져오기 성공");


            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            int id = element.getAsJsonObject().get("id").getAsInt();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            if(hasEmail){
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            br.close();

        } catch (IOException e) {
            throw new BaseException(KAKAO_LOGIN_FAIL);
        }
        //이메일 중복체크
        int userIdx = 0;

        try{
            System.out.println("이메일");
            System.out.println(email);
            if (userProvider.checkEmail(email) == 0){ // 계정 없음
                System.out.println("계정 없음");
                int err = 1/0;
            }
            else { //로그인 진행
                System.out.println("로그인 시도");
                User user = userProvider.checkEmailUser(email);
                System.out.println("login");
                userIdx = user.getUserIdx();
            }

            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx, jwt);
        } catch(ArithmeticException e) {
            throw new BaseException(USERS_NOT_EXISTS);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void postBanUser(String myId, String userId) throws BaseException {
        try {
            userDao.postBanUser(myId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void patchBanUser(String myId, String userId) throws BaseException {
        try {
            userDao.patchBanUser(myId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUser(String myId) throws BaseException {
        try {
            userDao.deleteUser(myId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchMy(String myId, PatchUserReq patchUserReq) throws BaseException {
        try {
            userDao.patchMy(myId, patchUserReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //본인인증 로그인
    // 번개장터는 전화번호로 본인인증을 받는다. 즉, 이메일, 아이디, 비밀번호가 없다. 비밀번호, 이름, 닉네임(상점 이름) 보유
//    public PostRegisterRes loginUser(PostRegisterReq postRegisterReq) throws BaseException {
//        try{
//            int userIdx = 0;
//            if(userProvider.checkEmail(postRegisterReq.getEmail()) == 0){ //회원가입 진행
//                UserEmail user = new UserEmail(postRegisterReq.getName(), postRegisterReq.getNickName(), postRegisterReq.getEmail());
//                userIdx = userDao.createUser(user);
////                createUserDefault(userIdx);
//            }
//            else { //로그인 진행
//                // 전화번호에 해당하는 유저
//                User user = userProvider.checkEmailUser(postRegisterReq.getEmail());
//                userIdx = user.getUserIdx();
//            }
//
//            //jwt 발급.
//            String jwt = jwtService.createJwt(userIdx);
//            return new PostRegisterRes(userIdx, jwt);
//
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

//    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
//        try{
//            int result = userDao.modifyUserName(patchUserReq);
//            if(result == 0){
//                throw new BaseException(MODIFY_FAIL_USERNAME);
//            }
//        } catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
}
