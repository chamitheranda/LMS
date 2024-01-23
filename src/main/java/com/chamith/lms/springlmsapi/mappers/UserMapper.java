package com.chamith.lms.springlmsapi.mappers;

import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into users(name, email, contact_number) values(#{name}, #{email}, #{contactNumber})")
    void insert(UserRequestDTO userRequestDTO);
}
