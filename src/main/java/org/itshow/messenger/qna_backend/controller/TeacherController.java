package org.itshow.messenger.qna_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.FaqDto;
import org.itshow.messenger.qna_backend.dto.TeacherDto;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.itshow.messenger.qna_backend.service.UserService;
import org.itshow.messenger.qna_backend.util.Response;
import org.itshow.messenger.qna_backend.util.Ulid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teacher")
public class TeacherController {
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final Ulid ulid;

    @PostMapping("/profile")
    public ResponseEntity<?> saveProfile(@RequestBody JsonNode request){
        try{
            JsonNode teacherNode = request.get("teacher");
            JsonNode faqNode = request.get("faq");

            if(teacherNode == null || !teacherNode.hasNonNull("teacherid")){
                return Response.badRequest("teacherid가 없음");
            }

            TeacherDto teacherDto = objectMapper.treeToValue(teacherNode, TeacherDto.class);
            userService.updateTeacher(teacherDto);

            if(faqNode != null && faqNode.isArray() && !faqNode.isEmpty()){
                List<Map<String, String>> faqs = objectMapper.readerForListOf(Map.class).readValue(faqNode);
                insertFaq(teacherDto.getTeacherid(), faqs);
            }
            return Response.ok("선생님 정보 저장 성공", null);
        }catch (JsonProcessingException e){
            return Response.badRequest("잘못된 JSON 형식");
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버에러");
        }
    }

    @PostMapping("/faq/{teacherid}")
    public ResponseEntity<?> insertFaq(@PathVariable("teacherid") String teacherid,
                                     @RequestBody List<Map<String, String>> faqs){
        try{
            for(Map<String, String> faq : faqs){
                String question = faq.get("question");
                String answer = faq.get("answer");

                if(question == null || answer == null){
                    continue;
                }

                FaqDto faqDto = new FaqDto();
                String faqid = ulid.nextUlid();

                faqDto.setFaqid(faqid);
                faqDto.setTeacherid(teacherid);
                faqDto.setQuestion(question);
                faqDto.setAnswer(answer);

                userService.insertFaq(faqDto);
            }
            return Response.ok("faq 저장 성공", null);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @GetMapping("/profile/{userid}") // 프로필 조회
    public ResponseEntity<?> selectProfile(@PathVariable("userid") String userid){
        try{
            Map<String, Object> profile = userService.selectUser(userid);

            if(profile == null){
                return Response.badRequest("사용자를 찾을 수 없음");
            }

            return Response.ok("선생님 프로필 조회 성공", profile);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }

    @GetMapping("/search")  // 선생님 목록 조회
    public ResponseEntity<?> searchUser(@RequestParam(name = "search", required = false) String search){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !(authentication.getPrincipal() instanceof UserDto reqUser)){
            return Response.unauthorized("인증 정보 없음");
        }

        try{
            if(search == null || search.trim().isEmpty()){
                search = "";
            }

            List<Map<String, Object>> result;
            if(reqUser.getUsertype() == UserDto.UserType.STUDENT){
                result = userService.searchTeacher(search);
            }else{
                result = userService.searchStudent(search);
            }

            return Response.ok("목록 조회 성공", result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.internalServerError("서버 오류");
        }
    }
}
