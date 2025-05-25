package org.itshow.messenger.qna_backend.dto;

import lombok.Data;

@Data
public class FileDto {  // 파일 정보
    public enum FileType{
        MESSAGE, SCHEDULE
    }

    private String fileid;
    private FileType filetype;
    private String targetid;    // messageid 또는 scheduleid
    private String url;
    private String name;
}
