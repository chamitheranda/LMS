package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.SingInCredientials;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private GenerateJWT generateJWT  ;

    public String addUser(UserRequestDTO userReqestDTO) {
        String encryptedPassword = passwordEncoder.encode(userReqestDTO.getPassword());
        userReqestDTO.setPassword(encryptedPassword);
        userMapper.insert(userReqestDTO);

        return "User details registered";
    }

    public SingInCredientials signIn(SignInRequestDTO signInRequestDTO) {
        String storedPassword = userMapper.selectPasswordByEmail(signInRequestDTO.getEmail());
        if(passwordEncoder.matches(signInRequestDTO.getPassword() , storedPassword)){
            String jwtToken = generateJWT.generateToken(signInRequestDTO.getEmail() ,
                    userMapper.selectPrivilegeLevelByEmail(signInRequestDTO.getEmail()));
            return new SingInCredientials(
                    jwtToken,
                    HttpStatus.OK
                    ) ;
        }else{
            return new SingInCredientials(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<StandardResponse> enroll(EnrollRequestDTO enrollRequestDTO) {
        if(userMapper.doesSubjectExist(enrollRequestDTO.getEmail() , enrollRequestDTO.getSubject()) && userMapper.doesEmailExist(enrollRequestDTO.getEmail())){
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "User is not present ",
                            "Subject already enrolled !!!!"
                    ), HttpStatus.OK);
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
