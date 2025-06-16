package org.itshow.messenger.qna_backend.controller;

import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.*;
import org.itshow.messenger.qna_backend.service.ChatService;
import org.itshow.messenger.qna_backend.service.ScheduleService;
import org.itshow.messenger.qna_backend.service.SettingService;
import org.itshow.messenger.qna_backend.service.UserService;
import org.itshow.messenger.qna_backend.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SettingController {
    private final SettingService settingService;
    private final UserService userService;
    private final ChatService chatService;
    private final ScheduleService scheduleService;
    private UserDto getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !(authentication.getPrincipal() instanceof UserDto)){
            return null;
        }
        return (UserDto) authentication.getPrincipal();
    }

    @GetMapping("/profile") // 설정 페이지 (마이페이지) 조회
    public ResponseEntity<?> selectSetting(){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            Map<String, Object> result = new HashMap<>();
            result.put("user", reqUser);

            String userid = reqUser.getUserid();
            if(reqUser.getUsertype() == UserDto.UserType.TEACHER){
                TeacherDto teacher = settingService.selectTeacher(userid);
                List<FaqDto> faqs = settingService.selectFaqs(userid);

                result.put("teacher", teacher);
                result.put("faqs", faqs);
            }

            AlarmSettingDto alarm = settingService.selectAlarm(userid);
            result.put("alarm", alarm);

            return Response.ok("프로필 조회 성공", result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @PatchMapping("/img")   // 프로필 이미지 수정
    public ResponseEntity<?> updateImgurl(@RequestBody Map<String, String> map){
        try{
            UserDto reqUser = getUser();
            if(reqUser == null){
                return Response.unauthorized("인증 정보 없음");
            }
            reqUser.setImgurl(map.get("imgurl"));
            settingService.updateImgurl(reqUser);

            return Response.ok("프로필 이미지 수정 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @PatchMapping("/teacher")   // 선생님 추가 프로필 수정
    public ResponseEntity<?> updateTeacher(@RequestBody TeacherDto teacher){
        try{
            userService.updateTeacher(teacher);

            return Response.ok("선생님 프로필 수정 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @PatchMapping("/faq")   // 선생님 faq 수정
    public ResponseEntity<?> updateFaq(@RequestBody List<FaqDto> faqs){
        try{
            for(FaqDto faq : faqs){
                settingService.updateFaq(faq);
            }

            return Response.ok("faq 수정 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @DeleteMapping("/faq")  // 선생님 faq 삭제
    public ResponseEntity<?> deleteFaq(@RequestBody List<FaqDto> faqs){
        try{
            for(FaqDto faq : faqs){
                settingService.deleteFaq(faq);
            }

            return Response.ok("faq 삭제 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @PatchMapping("/alarm")   // 알람 설정 변경
    public ResponseEntity<?> updateAlarm(@RequestBody AlarmSettingDto alarm){
        try{
            settingService.updateAlarm(alarm);
            return Response.ok("알람 설정 변경 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @DeleteMapping("/delete")   // 회원 탈퇴
    public ResponseEntity<?> deleteUser(){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            // schedule 파일 삭제
            List<ScheduleDto> schedules = scheduleService.selectScheduleList(reqUser.getUserid());
            for(ScheduleDto schedule : schedules){
                settingService.deleteFile(schedule.getScheduleid());
            }
            // 채팅방 나가기
            List<RoomDto> rooms = chatService.searchRoom(reqUser.getUserid(), reqUser.getUsertype(), "");
            for(RoomDto room : rooms){
                String roomid = room.getRoomid();
                chatService.updateRoom(roomid, reqUser.getUsertype().name());
                chatService.exitRoom(roomid, reqUser.getUserid());
            }
            settingService.deleteUser(reqUser);

            return Response.ok("회원 탈퇴 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }
}
