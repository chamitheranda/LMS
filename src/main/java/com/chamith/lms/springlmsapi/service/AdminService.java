package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserMapper userMapper ;

    public ResponseEntity<StandardResponse> updatePrivilege(String email) {
        if(userMapper.doesEmailExist(email)){
            userMapper.updatePrivilegeLevel(email);
            return new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Update Privilege",
                                "Update successfully !!!!"
                        ), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(
                        new StandardResponse(
                                204,
                                "email not found",
                                "update failed !!!!"
                        ), HttpStatus.NOT_FOUND);
            }
        }

    }

