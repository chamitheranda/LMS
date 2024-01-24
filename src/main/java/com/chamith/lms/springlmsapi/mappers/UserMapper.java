package com.chamith.lms.springlmsapi.mappers;

import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into users(name, email, contact_number , password) values(#{name}, #{email}, #{contactNumber},#{password})")
    void insert(UserRequestDTO userRequestDTO);

    @Select("SELECT password FROM users WHERE email = #{email}")
    String selectPasswordByEmail(String email);
}
