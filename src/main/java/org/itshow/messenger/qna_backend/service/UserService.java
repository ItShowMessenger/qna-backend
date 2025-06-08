package org.itshow.messenger.qna_backend.service;

import org.itshow.messenger.qna_backend.dto.UserDto;

public interface UserService {
    public UserDto selectEmail(String email);
    public void insertUser(UserDto dto);
}
