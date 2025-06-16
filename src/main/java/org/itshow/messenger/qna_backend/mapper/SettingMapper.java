package org.itshow.messenger.qna_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.itshow.messenger.qna_backend.dto.AlarmSettingDto;
import org.itshow.messenger.qna_backend.dto.FaqDto;
import org.itshow.messenger.qna_backend.dto.UserDto;

@Mapper
public interface SettingMapper {
    public AlarmSettingDto selectAlarm(String userid);
    public void updateAlarm(AlarmSettingDto dto);
    public void updateImgurl(UserDto dto);
    public FaqDto selectFaq(String faqid);
    public void updateFaq(FaqDto dto);
    public void deleteFaq(FaqDto dto);
    public void deleteFile(String scheduleid);
    public void deleteUser(UserDto dto);
}
