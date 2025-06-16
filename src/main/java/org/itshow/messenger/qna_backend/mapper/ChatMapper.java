package org.itshow.messenger.qna_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.itshow.messenger.qna_backend.dto.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface ChatMapper {
    public void insertMessage(MessageDto dto);
    public void insertFile(FileDto dto);
    public List<RoomDto> searchRoom(@Param("userid") String userid,
                                    @Param("usertype") String usertype,
                                    @Param("search") String search);
    public MessageDto selectLastMessage(String messageid);
    public int countUnRead(@Param("roomid") String roomid,
                           @Param("userid") String userid);
    public RoomDto selectRoom(String roomid);
    public void insertRoom(String roomid);
    public List<MessageDto> selectMessage(String roomid);
    public List<FileDto> selectFile(String messageid);
    public List<EmojiDto> selectEmojiList(String messageid);
    public void updateMessageRead(@Param("userid") String userid,
                                  @Param("roomid") String roomid);
    public MessageDto selectMessageId(String messageid);
    public void updateMessage(String messageid);
    public void deleteFile(String messageid);
    public void deleteRoom(String roomid);
    public void deleteEmoji(String messageid);
    public void updateRoom(@Param("roomid") String roomid,
                           @Param("usertype") String usertype);
    public void deleteEmojiOne(@Param("messageid") String messageid,
                               @Param("userid") String userid);
    public void insertEmoji(EmojiDto emoji);
    public EmojiDto selectEmoji(@Param("messageid") String messageid,
                                @Param("userid") String userid);
}
