package org.itshow.messenger.qna_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.itshow.messenger.qna_backend.dto.*;

import java.util.List;

@Mapper
public interface ChatMapper {
    public void insertMessage(MessageDto dto);
    public void insertFile(FileDto dto);
    public List<RoomDto> searchRoom(String userid, UserDto.UserType usertype, String search);
    public MessageDto selectLastMessage(String messageid);
    public int countUnRead(String roomid, String userid);
    public RoomDto selectRoom(String roomid);
    public void insertRoom(String roomid);
    public List<MessageDto> selectMessage(String roomid);
    public List<FileDto> selectFile(String messageid);
    public List<EmojiDto> selectEmojiList(String messageid);
    public void updateMessageRead(String userid, String roomid);
    public MessageDto selectMessageId(String messageid);
    public void updateMessage(String messageid);
    public void deleteFile(String messageid);
    public void deleteRoom(String roomid);
    public void deleteEmoji(String messageid);
    public void updateRoom(String roomid, UserDto.UserType usertype);
    public void deleteEmojiOne(String messageid, String userid);
    public void insertEmoji(EmojiDto emoji);
    public EmojiDto selectEmoji(String messageid, String userid);
}
