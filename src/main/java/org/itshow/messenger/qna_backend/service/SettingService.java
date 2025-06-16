package org.itshow.messenger.qna_backend.service;

import org.itshow.messenger.qna_backend.dto.AlarmSettingDto;
import org.itshow.messenger.qna_backend.dto.FaqDto;
import org.itshow.messenger.qna_backend.dto.TeacherDto;
import org.itshow.messenger.qna_backend.dto.UserDto;

import java.util.List;

public interface SettingService {
    public TeacherDto selectTeacher(String teacherid);
    public List<FaqDto> selectFaqs(String teacherid);
    public AlarmSettingDto selectAlarm(String userid);
    public void updateAlarm(AlarmSettingDto dto);
    public void updateImgurl(UserDto dto);
    public void updateFaq(FaqDto dto);
    public void deleteFaq(FaqDto dto);
    public void deleteFile(String scheduleid);
    public void deleteUser(UserDto dto);
}
