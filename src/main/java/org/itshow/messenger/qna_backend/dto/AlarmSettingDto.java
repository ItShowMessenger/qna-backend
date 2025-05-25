package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

@Data
public class AlarmSettingDto {  // 알람 설정
    private String userid;
    private boolean alarm;
    private boolean chat;   // 채팅 알림
    private boolean schedule;   // 스케쥴 알림
}
