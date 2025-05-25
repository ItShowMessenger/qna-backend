package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

@Data
public class FaqDto {   // 선생님 추가 프로필 : 예상 질문과 답변
    private String faqid;
    private String teacherid;
    private String question;
    private String answer;
}
