package org.itshow.messenger.qna_backend.service;

import org.itshow.messenger.qna_backend.mapper.UserMapper;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper mapper;

    @Override
    public UserDto selectEmail(String email) {
        return mapper.selectEmail(email);
    }
    @Override
    public void insertUser(UserDto dto) {
        // user 추가
        mapper.insertUser(dto);
    }
}
