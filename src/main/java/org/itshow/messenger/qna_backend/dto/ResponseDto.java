package org.itshow.messenger.qna_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
}
