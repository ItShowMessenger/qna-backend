package org.itshow.messenger.qna_backend.service;

import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.FileDto;
import org.itshow.messenger.qna_backend.dto.ScheduleDto;
import org.itshow.messenger.qna_backend.dto.SharedDto;
import org.itshow.messenger.qna_backend.mapper.ChatMapper;
import org.itshow.messenger.qna_backend.mapper.ScheduleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleMapper mapper;
    private final ChatMapper chatMapper;

    @Override
    public List<ScheduleDto> selectScheduleList(String userid) {
        return mapper.selectScheduleList(userid);
    }

    @Override
    public void insertSchedule(ScheduleDto dto) {
        mapper.insertSchedule(dto);
    }

    @Override
    public FileDto selectFileOne(String fileid) {
        return mapper.selectFileOne(fileid);
    }

    @Override
    public void updateFile(FileDto dto) {
        mapper.updateFile(dto);
    }

    @Override
    public void updateSchedule(ScheduleDto dto) {
        mapper.updateSchedule(dto);
    }

    @Override
    public void deleteFile(String scheduleid) {
        chatMapper.deleteFile(scheduleid);
    }

    @Override
    public void deleteSchedule(String scheduleid) {
        mapper.deleteSchedule(scheduleid);
    }

    @Override
    public void insertShared(SharedDto dto) {
        mapper.insertShared(dto);
    }

 @Override
    public void deleteShared(String scheduleid) {
        mapper.deleteShared(scheduleid);
    }
}
