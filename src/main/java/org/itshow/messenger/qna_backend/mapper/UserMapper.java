package org.itshow.messenger.qna_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface UserMapper {
    public UserDto selectEmail(String email);
    public void insertUser(UserDto dto);
}
