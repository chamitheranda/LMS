package com.chamith.lms.springlmsapi.mappers;

import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into users(name, email, contact_number , password) values(#{name}, #{email}, #{contactNumber},#{password})")
    void insert(UserRequestDTO userRequestDTO);

    @Select("SELECT password FROM users WHERE email = #{email}")
    String selectPasswordByEmail(String email);

    @Select("SELECT privilege_level FROM users WHERE email = #{email}")
    String selectPrivilegeLevelByEmail(String email);

    @Update("UPDATE users SET privilege_level = 'admin'  WHERE email = #{email} ")
    void updatePrivilegeLevel(String email);

    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email}")
    boolean doesEmailExist(String email);

    @Delete("DELETE FROM users WHERE email = #{email}")
    void deleteUserByEmail(String email);

    @Select("SELECT privilege_level FROM users WHERE email=#{email}")
    String getPrivilegeLevel(String email);
}
