package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserMapper userMapper ;

    public String updatePrivilege(String email) {
        userMapper.updatePrivilegeLevel(email);
        return "Successfully updated !!!!" ;
    }
}
