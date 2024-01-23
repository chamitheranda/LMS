package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper ;

    public String addUser(UserRequestDTO userReqestDTO) {
        userMapper.insert(userReqestDTO);
        return "User details registered";
    }
}
