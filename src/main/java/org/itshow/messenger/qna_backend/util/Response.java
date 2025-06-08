package org.itshow.messenger.qna_backend.util;

import org.itshow.messenger.qna_backend.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    public static <T> ResponseEntity<ResponseDto<T>> ok(String message, T data){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(true, message, data));
    }
    public static ResponseEntity<ResponseDto<Object>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDto<>(false, message, null));
    }

    public static ResponseEntity<ResponseDto<Object>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseDto<>(false, message, null));
    }

    public static ResponseEntity<ResponseDto<Object>> internalServerError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDto<>(false, message, null));
    }

    public static ResponseEntity<ResponseDto<Object>> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(false, message, null));
    }
}
