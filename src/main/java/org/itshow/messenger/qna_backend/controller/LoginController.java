package org.itshow.messenger.qna_backend.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.itshow.messenger.qna_backend.service.UserService;
import org.itshow.messenger.qna_backend.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final Pattern schoolEmail = Pattern.compile("^[a-zA-Z0-9._%+-]+@e-mirim\\.hs\\.kr$");
    private final ULID ulid = new ULID();

    @PostMapping("/api/login")
    public ResponseEntity<?> login(HttpServletRequest request){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!(principal instanceof UserDto firebase) || firebase.getEmail() == null){
            return Response.unauthorized("Firebase 토큰 인증 실패");
        }

        // 학교 이메일 확인
        if(!isSchoolEmail(firebase.getEmail())){
            return Response.forbidden("학교 이메일이 아닙니다.");
        }

        try{
            UserDto user = userService.selectEmail(firebase.getEmail());
            String message = "로그인 성공";

            // 자동 회원가입
            if(user == null){
                // ulid 생성
                String userid = ulid.nextULID();
                firebase.setUserid(userid);

                // usertype 구분
                if (firebase.getName() != null && firebase.getName().matches("^\\d{4}.*")) {
                    firebase.setUsertype(UserDto.UserType.STUDENT);
                    firebase.setName(firebase.getName().replaceFirst("^\\d{4}_", ""));
                } else {
                    firebase.setUsertype(UserDto.UserType.TEACHER);
                }

                // user 추가
                userService.insertUser(firebase);
                user = firebase;

                // 선생님 추가 정보 필요
                if(user.getUsertype() == UserDto.UserType.TEACHER){
                    message = "가입 성공";
                }
            }

            return Response.ok(message, user);

        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    private boolean isSchoolEmail(String email){
        // 학교 이메일
        return email != null && schoolEmail.matcher(email).matches();
    }
}
