package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmojiDto {
    private String messageid;
    private String userid;
    private String emoji;
    private LocalDateTime createdat;
}
