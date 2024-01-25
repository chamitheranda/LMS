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

    public ResponseEntity<StandardResponse> viewResults(String email) {
        if(userMapper.doesEmailExist(email)){
            List<ViewResultsResponseDTO > resultSet = userMapper.getAllResults(email);
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "This is the results ",
                            resultSet
                    ), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(
                    new StandardResponse(
                            204,
                            "No Results for = " + email ,
                            "Results Not Found  !!!!"
                    ), HttpStatus.NOT_FOUND);
        }
    }
}