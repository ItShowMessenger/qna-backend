package org.itshow.messenger.qna_backend.service;

import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.FaqDto;
import org.itshow.messenger.qna_backend.dto.TeacherDto;
import org.itshow.messenger.qna_backend.mapper.UserMapper;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper mapper;

    @Override
    public UserDto selectEmail(String email) {
        return mapper.selectEmail(email);
    }
    @Override
    public void insertUser(UserDto dto) {
        // user 추가
        mapper.insertUser(dto);
    }

    @Override
    public void insertTeacher(String teacherid) {
        // teacher 추가
        mapper.insertTeacher(teacherid);
    }

    @Override
    public void updateTeacher(TeacherDto dto) {
        mapper.updateTeacher(dto);
    }

    @Override
    public void insertFaq(FaqDto dto) {
        mapper.insertFaq(dto);
    }
}
