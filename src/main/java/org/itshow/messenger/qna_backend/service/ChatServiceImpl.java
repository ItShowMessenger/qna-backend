package org.itshow.messenger.qna_backend.service;

import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.*;
import org.itshow.messenger.qna_backend.mapper.ChatMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService{
    private final ChatMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void insertMessage(MessageDto dto) {
        mapper.insertMessage(dto);
    }

    @Override
    public void insertFile(FileDto dto) {
        mapper.insertFile(dto);
    }

    @Override
    public List<RoomDto> searchRoom(String userid, UserDto.UserType usertype, String search) {
        if(search == null || search.trim().isEmpty()){
            search = "";
        }

        return mapper.searchRoom(userid, usertype.name(), search);
    }

    @Override
    public MessageDto selectLastMessage(String lastmessageid) {
        return mapper.selectLastMessage(lastmessageid);
    }

    @Override
    public int countUnRead(String roomid, String userid) {
        return mapper.countUnRead(roomid, userid);
    }

    @Override
    public RoomDto selectInsertRoom(String roomid, String userid) {
        RoomDto room = mapper.selectRoom(roomid);
        if(room == null){
            mapper.insertRoom(roomid);
            room = mapper.selectRoom(roomid);
            enterRoom(roomid, userid);
        }
        return room;
    }

    @Override
    public List<MessageDto> selectMessage(String roomid) {
        return mapper.selectMessage(roomid);
    }

    @Override
    public List<FileDto> selectFile(String messageid) {
        return mapper.selectFile(messageid);
    }

    @Override
    public List<EmojiDto> selectEmojiList(String messageid) {
        return mapper.selectEmojiList(messageid);
    }

    @Override
    public void updateMessageRead(String userid, String roomid) {
        mapper.updateMessageRead(userid, roomid);
    }

    @Override
    public MessageDto selectMessageId(String messageid) {
        return mapper.selectMessageId(messageid);
    }

    @Override
    public void deleteMessage(String messageid) {
        mapper.updateMessage(messageid);
        mapper.deleteFile(messageid);
        mapper.deleteEmoji(messageid);
    }

    @Override
    public void updateRoom(String roomid, String usertype) {
        mapper.updateRoom(roomid, usertype);
    }

    @Override
    public void deleteRoom(String roomid) {
        List<MessageDto> messages = mapper.selectMessage(roomid);
        for(MessageDto message : messages){
            mapper.deleteFile(message.getMessageid());
        }
        mapper.deleteRoom(roomid);
    }

    @Override
    public void enterRoom(String roomid, String userid) {
        Map<String, Object> msg = Map.of(
                "type", "enter",
                "roomid", roomid,
                "userid", userid,
                "message", userid + "님이 입장하였습니다."
        );
        messagingTemplate.convertAndSend("/queue/chat/room/" + roomid, msg);
    }

    @Override
    public void exitRoom(String roomid, String userid) {
        Map<String, Object> msg = Map.of(
                "type", "exit",
                "roomid", roomid,
                "userid", userid,
                "message", userid + "님이 퇴장하였습니다."
        );
        messagingTemplate.convertAndSend("/queue/chat/room/" + roomid, msg);
    }

    @Override
    public void deleteEmojiOne(String messageid, String userid) {
        mapper.deleteEmojiOne(messageid, userid);
    }

    @Override
    public void insertEmoji(EmojiDto dto) {
        String messageid = dto.getMessageid();
        String userid = dto.getUserid();
        EmojiDto emoji = mapper.selectEmoji(messageid, userid);
        if(emoji != null){
            deleteEmojiOne(messageid, userid);
        }

        mapper.insertEmoji(dto);
    }
}
