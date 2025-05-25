package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {  // 사용자 정보
    public enum UserType{
        STUDENT, TEACHER
    }

    private String userid;
    private String email;
    private String name;
    private String imgurl;  // 프로필 이미지
    private UserType usertype;    // student or teacher
}
