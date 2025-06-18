package org.itshow.messenger.qna_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.itshow.messenger.qna_backend.dto.FileDto;
import org.itshow.messenger.qna_backend.dto.ScheduleDto;
import org.itshow.messenger.qna_backend.dto.SharedDto;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    public List<ScheduleDto> selectScheduleList(String userid);
    public void insertSchedule(ScheduleDto dto);
    public FileDto selectFileOne(String fileid);
    public void updateFile(FileDto dto);
    public void updateSchedule(ScheduleDto dto);
    public void deleteSchedule(String scheduleid);
    public void insertShared(SharedDto dto);
    public void deleteShared(String scheduleid);
}
