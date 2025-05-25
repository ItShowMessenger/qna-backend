package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

@Data
public class SharedDto {    // schedule 공유받은 userid 목록
    private String scheduleid;
    private String studentid;   // = userid (공유받은 학생들 아이디)
}
