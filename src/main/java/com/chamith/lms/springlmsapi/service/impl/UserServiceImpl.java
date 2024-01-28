package com.chamith.lms.springlmsapi.service.impl;

import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.UserService;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper ;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public ResponseEntity<StandardResponse> enroll(EnrollRequestDTO enrollRequestDTO, String email) {
        if(userMapper.doesSubjectExist(email , enrollRequestDTO.getSubject())
                && userMapper.doesEmailExist(email)){
            logger.warn("Already enrolled for subject "+enrollRequestDTO.getSubject());
            return new ResponseEntity<>(
                    new StandardResponse(
                            417,
                            "User Enrolled subject =  "+enrollRequestDTO.getSubject(),
                            "Already enrolled for this subject!!!!"
                    ), HttpStatus.EXPECTATION_FAILED);
        }else {
            userMapper.addCourse(enrollRequestDTO.getSubject() , email);
            logger.info("Enrolled course = "+enrollRequestDTO.getSubject()+" user = "+email);
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "Enrolled course = "+enrollRequestDTO.getSubject(),
                            "Enrolled successfully  !!!!"
                    ), HttpStatus.OK);
        }
    }

}