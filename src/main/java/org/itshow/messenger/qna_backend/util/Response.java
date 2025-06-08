package org.itshow.messenger.qna_backend.util;

import org.itshow.messenger.qna_backend.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    public static <T> ResponseEntity<ResponseDto<T>> ok(String message, T data){
        // 200 성공
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(true, message, data));
    }
    public static ResponseEntity<ResponseDto<Object>> unauthorized(String message) {
        // 401 인증 실패
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDto<>(false, message, null));
    }

    public static ResponseEntity<ResponseDto<Object>> forbidden(String message) {
        // 403 권한 없음
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseDto<>(false, message, null));
    }

    public static ResponseEntity<ResponseDto<Object>> internalServerError(String message) {
        // 500 서버 오류
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDto<>(false, message, null));
    }

    public static ResponseEntity<ResponseDto<Object>> badRequest(String message) {
        // 400 잘못된 요청
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(false, message, null));
    }
}
