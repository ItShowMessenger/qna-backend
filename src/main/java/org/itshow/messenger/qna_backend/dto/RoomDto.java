package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

import java.sql.Statement;
import java.util.List;

@Data
public class RoomDto {  // 채팅방 정보
    public enum Status {
        STUDENT, TEACHER    // student = 학생 탈퇴(선생님만 참가), teacher = 선생님 탈퇴(학생만 참가)
    }

    private String roomid;  // 형식 : studentid_teacherid
    // room 이름 : 대화 상대 이름
    private String lastmessageid;
    private Status status;  // null = 전부 참가
}
