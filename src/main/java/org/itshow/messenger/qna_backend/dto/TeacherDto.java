package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeacherDto {   // 선생님 추가 정보
    private String teacherid; // == userid
    private String subject;
    private String office;  // 교무실 위치
}