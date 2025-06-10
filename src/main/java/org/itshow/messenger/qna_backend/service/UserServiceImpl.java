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
        List<TeacherDto> teachers = mapper.selectAllTeacher();
        List<UserDto> users;

        if(search == null || search.trim().isEmpty()){
            users = new ArrayList<>();
            for(TeacherDto teacher: teachers){
                UserDto user = mapper.selectId(teacher.getTeacherid());
                users.add(user);
            }
        }else{
            users  = mapper.selectSearch(search);
        }

        Map<String, UserDto> userMap = new HashMap<>();
        for(UserDto user : users){
            userMap.put(user.getUserid(), user);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for(TeacherDto teacher : teachers){
            UserDto user = userMap.get(teacher.getTeacherid());
            if(user != null){
                Map<String, Object> map = new HashMap<>();
                map.put("user", user);
                map.put("teacher", teacher);
                result.add(map);
            }
        }

        return result;
    }
}
