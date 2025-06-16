package org.itshow.messenger.qna_backend.service;

import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.AlarmSettingDto;
import org.itshow.messenger.qna_backend.dto.FaqDto;
import org.itshow.messenger.qna_backend.dto.TeacherDto;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.itshow.messenger.qna_backend.mapper.SettingMapper;
import org.itshow.messenger.qna_backend.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService{
    private final UserMapper userMapper;
    private final SettingMapper settingMapper;
    @Override
    public TeacherDto selectTeacher(String teacherid) {
        return userMapper.selectTeacher(teacherid);
    }

    @Override
    public List<FaqDto> selectFaqs(String teacherid) {
        return userMapper.selectFaqs(teacherid);
    }

    @Override
    public AlarmSettingDto selectAlarm(String userid) {
        return settingMapper.selectAlarm(userid);
    }

    @Override
    public void updateAlarm(AlarmSettingDto dto) {
        if(!dto.isAlarm()){
            dto.setChat(false);
            dto.setSchedule(false);
        }
        settingMapper.updateAlarm(dto);
    }

    @Override
    public void updateImgurl(UserDto dto) {
        settingMapper.updateImgurl(dto);
    }

    @Override
    public void updateFaq(FaqDto dto) {
        FaqDto faq = settingMapper.selectFaq(dto.getFaqid());
        if(faq == null){
            userMapper.insertFaq(dto);
        }else{
            settingMapper.updateFaq(dto);
        }
    }

    @Override
    public void deleteFaq(FaqDto dto) {
        settingMapper.deleteFaq(dto);
    }

    @Override
    public void deleteFile(String scheduleid) {
        settingMapper.deleteFile(scheduleid);
    }

    @Override
    public void deleteUser(UserDto dto) {
        settingMapper.deleteUser(dto);
    }
}
