package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MessageDto {   // 보낸 메시지
    private String messageid;
    private String roomid;
    private String senderid;
    private String text;
    private boolean hasfile;    // 파일 없으면 text 있어야함.
    private boolean read;
    private LocalDateTime createdat;
}
