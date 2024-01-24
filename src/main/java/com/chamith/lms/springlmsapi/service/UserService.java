package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.SingInCredientials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            String jwtToken = generateJWT.generateToken(signInRequestDTO.getEmail());
            return new SingInCredientials(
                    jwtToken,
                    HttpStatus.OK
            ) ;
        }else{
            return new SingInCredientials(HttpStatus.UNAUTHORIZED);
        }
    }
}
