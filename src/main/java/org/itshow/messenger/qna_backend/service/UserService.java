package org.itshow.messenger.qna_backend.service;

import org.itshow.messenger.qna_backend.dto.FaqDto;
import org.itshow.messenger.qna_backend.dto.TeacherDto;
import org.itshow.messenger.qna_backend.dto.UserDto;

public interface UserService {
    public UserDto selectEmail(String email);
    public void insertUser(UserDto dto);
    public void insertTeacher(String teacherid);
    public void updateTeacher(TeacherDto dto);
    public void insertFaq(FaqDto dto);
}
