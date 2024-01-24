package com.chamith.lms.springlmsapi.controller;

import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.UserService;
import com.chamith.lms.springlmsapi.util.SingInCredientials;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
class SignUpController {

    @Autowired
    private UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<StandardResponse> signUp(@RequestBody UserRequestDTO userReqestDTO){
        String msg = userService.addUser(userReqestDTO);
        return new ResponseEntity<>(
                new StandardResponse(
                        201,
                        "Successfully sign up",
                        msg
                ), HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<StandardResponse> signIn(@RequestBody SignInRequestDTO signInRequestDTO){
        SingInCredientials singInCredientials = userService.signIn(signInRequestDTO);
        if (singInCredientials.getHttpStatus().equals(HttpStatus.OK)){
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "Successfully sign in !!!!",
                            singInCredientials.getToken()
                    ), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            "Sign in failed !!!!"
                    ), HttpStatus.UNAUTHORIZED);
        }

    }
}
