package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.dto.responseDTO.ViewResultsResponseDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.SingInCredientials;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper ;

    public ResponseEntity<StandardResponse> enroll(EnrollRequestDTO enrollRequestDTO) {
        if(userMapper.doesSubjectExist(enrollRequestDTO.getEmail() , enrollRequestDTO.getSubject())
                && userMapper.doesEmailExist(enrollRequestDTO.getEmail())){
            return new ResponseEntity<>(
                    new StandardResponse(
                            417,
                            "User is not present ",
                            "Subject already enrolled !!!!"
                    ), HttpStatus.EXPECTATION_FAILED);
        }else {
            userMapper.addCourse(enrollRequestDTO);
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "enrolled"+enrollRequestDTO.getSubject(),
                            "Enrolled successfully  !!!!"
                    ), HttpStatus.OK);
        }
    }

}