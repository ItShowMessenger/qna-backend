package org.itshow.messenger.qna_backend.service;

import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.FaqDto;
import org.itshow.messenger.qna_backend.dto.TeacherDto;
import org.itshow.messenger.qna_backend.mapper.UserMapper;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Object> selectUser(String userid) {
        Map<String, Object> result = new HashMap<>();

        UserDto user = mapper.selectId(userid);
        if(user == null) return null;
        result.put("user", user);

        if(user.getUsertype() == UserDto.UserType.TEACHER){
            TeacherDto teacher = mapper.selectTeacher(userid);
            List<FaqDto> faqs = mapper.selectFaqs(userid);

            result.put("teacher", teacher);
            result.put("faqs", faqs);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> searchTeacher(String search) {
        List<UserDto> users = mapper.selectSearchTeacher(search);

        List<Map<String, Object>> result = new ArrayList<>();
        for(UserDto user : users){
            TeacherDto teacher = mapper.selectTeacher(user.getUserid());
            if(teacher != null){
                Map<String, Object> map = new HashMap<>();
                map.put("user", user);
                map.put("teacher", teacher);
                result.add(map);
            }
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> searchStudent(String search) {
        List<UserDto> users = mapper.selectSearchStudent(search);

        List<Map<String, Object>> result = new ArrayList<>();
        for(UserDto user : users){
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            result.add(map);
        }

        return result;
    }
}
