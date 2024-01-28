package com.chamith.lms.springlmsapi.controller;

import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
import com.chamith.lms.springlmsapi.service.UserService;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GenerateJWT generateJWT ;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/enroll")
    public ResponseEntity<StandardResponse> enrollCourses(@RequestHeader("AuthenticationHeader") String accessToken ,
                                                          @RequestBody EnrollRequestDTO enrollRequestDTO ){
        if(generateJWT.validateToken(accessToken).isAuthenticationStatus()){
            String email = generateJWT.extractSubject(accessToken) ;
            return  userService.enroll(enrollRequestDTO , email);
        }else{
            logger.warn("Unauthorized Access");
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            "Sign in failed !!!!"
                    ), HttpStatus.UNAUTHORIZED);
        }
    }
}
