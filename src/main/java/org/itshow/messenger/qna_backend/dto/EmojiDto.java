package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmojiDto { // 이모지 정보
    private String messageid;
    private String userid;
    private String emoji;
    private LocalDateTime createdat;
}
