package org.itshow.messenger.qna_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.FileDto;
import org.itshow.messenger.qna_backend.dto.ScheduleDto;
import org.itshow.messenger.qna_backend.dto.SharedDto;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.itshow.messenger.qna_backend.service.ChatService;
import org.itshow.messenger.qna_backend.service.ScheduleService;
import org.itshow.messenger.qna_backend.util.Response;
import org.itshow.messenger.qna_backend.util.Ulid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ChatService chatService;
    private final ObjectMapper objectMapper;
    private final Ulid ulid;

    private UserDto getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !(authentication.getPrincipal() instanceof UserDto)){
            return null;
        }
        return (UserDto) authentication.getPrincipal();
    }

    @PostMapping("/create") // 일정 추가
    public ResponseEntity<?> insertSchedule(@RequestBody JsonNode jsonNode){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        ScheduleDto schedule = objectMapper.convertValue(jsonNode.get("schedule"), ScheduleDto.class);
        if(!schedule.getUserid().equals(reqUser.getUserid())){
            return Response.badRequest("사용자 접근 오류");
        }

        List<FileDto> files = new ArrayList<>();
        if(jsonNode.has("file") && jsonNode.get("file").isArray()){
            for(JsonNode node : jsonNode.get("file")){
                files.add(objectMapper.convertValue(node, FileDto.class));
            }
        }

        try{
            schedule.setUserid(reqUser.getUserid());
            schedule.setScheduleid(ulid.nextUlid());
            scheduleService.insertSchedule(schedule);
            for(FileDto file : files){
                file.setFileid(ulid.nextUlid());
                file.setFiletype(FileDto.FileType.SCHEDULE);
                file.setTargetid(schedule.getScheduleid());
                chatService.insertFile(file);
            }

            return Response.ok("일정 추가 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @GetMapping("/read")    // 일정 조회
    public ResponseEntity<?> selectSchedule(){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            List<Map<String, Object>> result = new ArrayList<>();
            List<ScheduleDto> schedules = scheduleService.selectScheduleList(reqUser.getUserid());
            for(ScheduleDto schedule : schedules){
                List<FileDto> files = chatService.selectFile(schedule.getScheduleid());

                Map<String, Object> map = new HashMap<>();
                map.put("schedule", schedule);
                map.put("file", files);
                result.add(map);
            }

            return Response.ok("일정 조회 성공", result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @PatchMapping("/update")    // 일정 수정
    public ResponseEntity<?> updateSchedule(@RequestBody JsonNode jsonNode){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        ScheduleDto schedule = objectMapper.convertValue(jsonNode.get("schedule"), ScheduleDto.class);
        if(!schedule.getUserid().equals(reqUser.getUserid())){
            return Response.forbidden("수정 권한 없음");
        }

        List<FileDto> files = new ArrayList<>();
        if(jsonNode.has("files") && jsonNode.get("files").isArray()){
            for(JsonNode node : jsonNode.get("files")){
                files.add(objectMapper.convertValue(node, FileDto.class));
            }
        }

        try{
            scheduleService.updateSchedule(schedule);

            for(FileDto file : files){
                if(file.getFileid() != null
                        && scheduleService.selectFileOne(file.getFileid()) != null){
                    scheduleService.updateFile(file);
                }else{
                    file.setTargetid(schedule.getScheduleid());
                    chatService.insertFile(file);
                }
            }

            return Response.ok("일정 수정 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @DeleteMapping("/delete")   // 일정 삭제
    public ResponseEntity<?> deleteSchedule(@RequestBody ScheduleDto schedule){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        if(!schedule.getUserid().equals(reqUser.getUserid())){
            return Response.forbidden("삭제 권한 없음");
        }

        try {
            scheduleService.deleteFile(schedule.getScheduleid());
            scheduleService.deleteSchedule(schedule.getScheduleid());

            return Response.ok("일정 삭제 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @PostMapping("/share")  // 선생님 일정 공유
    public ResponseEntity<?> insertShared(@RequestBody SharedDto shared){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        if(reqUser.getUsertype() != UserDto.UserType.TEACHER){
            return Response.forbidden("선생님만 공유 가능");
        }

        try{
                scheduleService.insertShared(shared);

            return Response.ok("일정 공유 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @DeleteMapping("/share/{scheduleid}") // 선생님 일정 공유 삭제
    public ResponseEntity<?> deleteShared(@PathVariable("scheduleid") String scheduleid){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        if(reqUser.getUsertype() != UserDto.UserType.TEACHER){
            return Response.forbidden("선생님만 공유 삭제");
        }

        try{
            scheduleService.deleteShared(scheduleid);

            return Response.ok("공유 일정 삭제 성공", null);
        }catch (Exception e){
            return Response.internalServerError("서버 오류");
        }
    }
}
