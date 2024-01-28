package com.chamith.lms.springlmsapi.controller;

import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.service.AuthService;
import com.chamith.lms.springlmsapi.util.SingInCredientials;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @PostMapping("/signUp")
    public ResponseEntity<StandardResponse> signUp(@RequestBody UserRequestDTO userReqestDTO){
        String msg = authService.addUser(userReqestDTO);
        return new ResponseEntity<>(
                new StandardResponse(
                        201,
                        "Successfully sign up",
                        msg
                ), HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<StandardResponse> signIn(@RequestBody SignInRequestDTO signInRequestDTO){
        SingInCredientials singInCredientials = authService.signIn(signInRequestDTO);
        if (singInCredientials.getHttpStatus().equals(HttpStatus.OK)){
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "Successfully sign in !!!!",
                            singInCredientials.getToken()
                    ), HttpStatus.OK);
        }else {
            logger.warn("Unauthorized Access");
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            singInCredientials.getHttpStatus()
                    ), HttpStatus.UNAUTHORIZED);
        }

    }
}
