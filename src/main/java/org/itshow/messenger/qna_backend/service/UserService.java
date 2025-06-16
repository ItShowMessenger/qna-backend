package org.itshow.messenger.qna_backend.service;

import org.itshow.messenger.qna_backend.dto.FaqDto;
import org.itshow.messenger.qna_backend.dto.TeacherDto;
import org.itshow.messenger.qna_backend.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    public UserDto selectEmail(String email);
    public void insertUser(UserDto dto);
    public void insertTeacher(String teacherid);
    public void updateTeacher(TeacherDto dto);
    public void insertFaq(FaqDto dto);
    public Map<String, Object> selectUser(String userid);
    public List<Map<String, Object>> searchTeacher(String search);
    public List<Map<String, Object>> searchStudent(String search);
    public void insertAlarmSetting(String userid);
}
