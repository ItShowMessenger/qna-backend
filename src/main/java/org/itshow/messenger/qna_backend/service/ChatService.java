package org.itshow.messenger.qna_backend.service;

import org.itshow.messenger.qna_backend.dto.*;

import java.util.List;

public interface ChatService {
    public void insertMessage(MessageDto dto);
    public void insertFile(FileDto dto);
    public List<RoomDto> searchRoom(String userid, UserDto.UserType usertype, String search);
    public MessageDto selectLastMessage(String lastmessageid);
    public int countUnRead(String roomid, String userid);
    public RoomDto selectInsertRoom(String roomid, String userid, String usertype);
    public List<MessageDto> selectMessage(String roomid);
    public List<FileDto> selectFile(String messageid);
    public List<EmojiDto> selectEmojiList(String messageid);
    public void updateMessageRead(String userid, String roomid);
    public MessageDto selectMessageId(String messageid);
    public void deleteMessage(String messageid);
    public void updateRoom(String roomid, String usertype);
    public void deleteRoom(String roomid);
    public void enterRoom(String roomid, String userid);
    public void exitRoom(String roomid, String userid);
    public void deleteEmojiOne(String messageid, String userid);
    public void insertEmoji(EmojiDto dto);
}
