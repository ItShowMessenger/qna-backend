package org.itshow.messenger.qna_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.FileDto;
import org.itshow.messenger.qna_backend.dto.MessageDto;
import org.itshow.messenger.qna_backend.service.ChatService;
import org.itshow.messenger.qna_backend.util.Ulid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ObjectMapper mapper;
    private final Ulid ulid;

    @MessageMapping("/chat/message")   // 메시지 송수신 (text, file)
    public void sendMessage(@Payload JsonNode payload){
        MessageDto message = mapper.convertValue(payload.get("message"), MessageDto.class);
        List<FileDto> files = new ArrayList<>();
        if(payload.has("files") && payload.get("files").isArray()){
            for(JsonNode node : payload.get("files")){
                files.add(mapper.convertValue(node, FileDto.class));
            }
        }

        String text = message.getText();
        if((text == null || text.isEmpty()) && files.isEmpty()){
            messagingTemplate.convertAndSend("/queue/chat/error/" + message.getRoomid(), Map.of(
                    "type", "error",
                    "message", "메시지 내용 없음"
            ));
            return ;
        }

        message.setMessageid(ulid.nextUlid());
        chatService.insertMessage(message);
        for(FileDto file : files){
            file.setFileid(ulid.nextUlid());
            file.setFiletype(FileDto.FileType.MESSAGE);
            file.setTargetid(message.getMessageid());
            chatService.insertFile(file);
        }

        Map<String, Object> res = Map.of(
                "message", message,
                "files", files
        );
        messagingTemplate.convertAndSend("/queue/chat/room/" + message.getRoomid(), res);
    }

    @MessageMapping("/chat/read")   // 채팅 읽음 처리
    public void readMessage(@Payload JsonNode jsonNode){
        String userid = jsonNode.get("userid").asText();
        String roomid = jsonNode.get("roomid").asText();

        chatService.updateMessageRead(userid, roomid);
    }
}
