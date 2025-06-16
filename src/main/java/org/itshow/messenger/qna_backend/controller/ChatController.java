package org.itshow.messenger.qna_backend.controller;


import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.*;
import org.itshow.messenger.qna_backend.service.ChatService;
import org.itshow.messenger.qna_backend.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    private UserDto getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !(authentication.getPrincipal() instanceof UserDto)){
            return null;
        }
        return (UserDto) authentication.getPrincipal();
    }

    @GetMapping("/search")  // 채팅방 목록 조회
    public ResponseEntity<?> searchChat(@RequestParam(name = "search", required = false) String search){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            List<Map<String, Object>> result = new ArrayList<>();

            List<RoomDto> rooms = chatService.searchRoom(reqUser.getUserid(),reqUser.getUsertype(), search);
            for(RoomDto room : rooms){
                MessageDto message  = chatService.selectLastMessage(room.getLastmessageid());
                int unread = chatService.countUnRead(room.getRoomid(), reqUser.getUserid());

                Map<String, Object> map = new HashMap<>();
                map.put("room", room);
                map.put("message", message);
                map.put("unread", unread);

                result.add(map);
            }

            return Response.ok("채팅방 목록 조회 성공", result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @GetMapping("/{roomid}")    // 채팅방 자동 생성 + 대화 기록 조회
    public ResponseEntity<?> Chat(@PathVariable("roomid") String roomid){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            List<Map<String, Object>> result = new ArrayList<>();

            RoomDto room = chatService.selectInsertRoom(roomid, reqUser.getUserid());
            result.add(Map.of(
                    "room", room
            ));

            List<MessageDto> messages = chatService.selectMessage(roomid);
            for(MessageDto message : messages){
                String messageid = message.getMessageid();
                List<FileDto> files = chatService.selectFile(messageid);
                List<EmojiDto> emojis = chatService.selectEmojiList(messageid);

                Map<String, Object> map = new HashMap<>();
                map.put("message", message);
                map.put("file", files);
                map.put("emoji", emojis);

                result.add(map);
            }

            return Response.ok("채팅방 입장 성공", result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @DeleteMapping("/message/delete/{messageid}")  // 메시지 삭제
    public ResponseEntity<?> deleteMessage(@PathVariable("messageid") String messageid){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            MessageDto message = chatService.selectMessageId(messageid);
            if(!message.getSenderid().equals(reqUser.getUserid())){
                return Response.forbidden("내가 작성한 메시지가 아님");
            }

            LocalDateTime createdat = message.getCreatedat();
            if(createdat.plusMinutes(5).isBefore(LocalDateTime.now())){
                return Response.forbidden("5분 이내에만 메시지 삭제 가능");
            }

            chatService.deleteMessage(messageid);

            return Response.ok("메시지 삭제 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @GetMapping("/leave/{roomid}")  // 채팅방 나가기 + 삭제
    public ResponseEntity<?> leaveRoom(@PathVariable("roomid")String roomid){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            RoomDto room = chatService.selectInsertRoom(roomid, reqUser.getUserid());
            if(room == null){
                return Response.badRequest("채팅방이 존재하지 않음");
            }

            RoomDto.Status status = room.getStatus();
            if(status == null){
                chatService.updateRoom(roomid, reqUser.getUsertype().name());
                chatService.exitRoom(roomid, reqUser.getUserid());
            }else if (status.name().equals(reqUser.getUsertype().name())){
                chatService.deleteRoom(roomid);
            }else{
                return Response.badRequest("이미 나간 채팅방");
            }
        }catch(Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }

        return Response.ok("채팅방 나가기 성공", null);
    }

    @PostMapping("/message/emoji/{messageid}")  // 메시지에 이모지 반응 추가
    public ResponseEntity<?> insertEmoji(@PathVariable("messageid") String messageid,
                                         @RequestBody Map<String, String> req){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            String reqEmoji = req.get("emoji");
            if(reqEmoji == null || reqEmoji.trim().isEmpty()){
                return Response.badRequest("이모지 없음");
            }

            EmojiDto emoji = new EmojiDto();
            emoji.setMessageid(messageid);
            emoji.setUserid(reqUser.getUserid());
            emoji.setEmoji(reqEmoji);
            emoji.setCreatedat(LocalDateTime.now());

            chatService.insertEmoji(emoji);

            return Response.ok("이모지 추가 성공", emoji);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @DeleteMapping("/message/emoji/{messageid}")    // 메시지에 이모지 반응 제거
    public ResponseEntity<?> deleteEmoji(@PathVariable("messageid")String messageid){
        UserDto reqUser = getUser();
        if(reqUser == null){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            chatService.deleteEmojiOne(messageid, reqUser.getUserid());

            return Response.ok("이모지 삭제 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }
}
