package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleDto {  // 일정 정보
    private String scheduleid;
    private String userid;  // 작성자 id
    private String title;
    private String content;
    private String date;    // YYYY-MM-DD
    private String time;    // HH:mm 또는 ?교시
    private LocalDateTime alarm;  // LocalDateTime, 알람 설정한 경우만
}
