package com.chamith.lms.springlmsapi.controller;

import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
import com.chamith.lms.springlmsapi.service.UserService;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService ;

    @Autowired
    private GenerateJWT generateJWT ;

    @PostMapping("/enroll")
    public ResponseEntity<StandardResponse> enrollCourses(@RequestHeader("AuthenticationHeader") String accessToken ,
                                                          @RequestBody EnrollRequestDTO enrollRequestDTO ){
        if(generateJWT.validateToken(accessToken).isAuthenticationStatus()){
            return  userService.enroll(enrollRequestDTO);
        }else{
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            "Sign in failed !!!!"
                    ), HttpStatus.UNAUTHORIZED);
        }

    }
}
