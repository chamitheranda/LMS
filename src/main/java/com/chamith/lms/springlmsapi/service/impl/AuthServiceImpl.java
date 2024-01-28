package com.chamith.lms.springlmsapi.service.impl;

import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.AuthService;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.SingInCredientials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private GenerateJWT generateJWT  ;

    @Autowired
    private UserMapper userMapper ;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public String addUser(UserRequestDTO userReqestDTO) {
        String encryptedPassword = passwordEncoder.encode(userReqestDTO.getPassword());
        userReqestDTO.setPassword(encryptedPassword);
        userMapper.insert(userReqestDTO);
        MDC.put("user.email", userReqestDTO.getEmail());
        try {
            logger.info("User successfully SignUp "+userReqestDTO.getEmail());
        }finally {
            MDC.clear();
        }
        return "User details registered";
    }

    @Override
    public SingInCredientials signIn(SignInRequestDTO signInRequestDTO) {
        String storedPassword = userMapper.selectPasswordByEmail(signInRequestDTO.getEmail());
        if(passwordEncoder.matches(signInRequestDTO.getPassword() , storedPassword)){
            String jwtToken = generateJWT.generateToken(signInRequestDTO.getEmail() ,
                    userMapper.selectPrivilegeLevelByEmail(signInRequestDTO.getEmail()));
                    MDC.put("email", signInRequestDTO.getEmail());
                    try {
                        logger.info("User successfully Sign IN " + signInRequestDTO.getEmail());
                    }finally {
                        MDC.clear();
                    }
            return new SingInCredientials(
                    jwtToken,
                    HttpStatus.OK
            ) ;
        }else{
            return new SingInCredientials(HttpStatus.UNAUTHORIZED);
        }
    }
}
