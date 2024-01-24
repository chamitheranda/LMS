package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    public String addUser(UserRequestDTO userReqestDTO) {
        String encryptedPassword = passwordEncoder.encode(userReqestDTO.getPassword());
        userReqestDTO.setPassword(encryptedPassword);
        userMapper.insert(userReqestDTO);

        return "User details registered";
    }
}
