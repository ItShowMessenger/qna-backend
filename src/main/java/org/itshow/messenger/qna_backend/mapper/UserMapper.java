package org.itshow.messenger.qna_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.itshow.messenger.qna_backend.dto.FaqDto;
import org.itshow.messenger.qna_backend.dto.TeacherDto;
import org.itshow.messenger.qna_backend.dto.UserDto;

import java.util.List;

@Mapper
public interface UserMapper {
    public UserDto selectEmail(String email);
    public void insertUser(UserDto dto);
    public void insertTeacher(String teacherid);
    public void updateTeacher(TeacherDto dto);
    public void insertFaq(FaqDto dto);
    public UserDto selectId(String userid);
    public TeacherDto selectTeacher(String teacherid);
    public List<FaqDto> selectFaqs(String teacherid);
    public List<UserDto> selectSearchTeacher(String search);
    public List<UserDto> selectSearchStudent(String search);
    public void insertAlarmSetting(String userid);
}
