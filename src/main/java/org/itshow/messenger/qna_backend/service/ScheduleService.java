package org.itshow.messenger.qna_backend.service;

import org.itshow.messenger.qna_backend.dto.FileDto;
import org.itshow.messenger.qna_backend.dto.ScheduleDto;
import org.itshow.messenger.qna_backend.dto.SharedDto;

import java.util.List;

public interface ScheduleService {
    public List<ScheduleDto> selectScheduleList(String userid);
    public void insertSchedule(ScheduleDto dto);
    public FileDto selectFileOne(String fileid);
    public void updateFile(FileDto dto);
    public void updateSchedule(ScheduleDto dto);
    public void deleteFile(String scheduleid);
    public void deleteSchedule(String scheduleid);
    public void insertShared(SharedDto dto);
    public void deleteShared(String scheduleid);
}
