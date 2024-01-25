package com.chamith.lms.springlmsapi.controller;

import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.service.UserService;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.SingInCredientials;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
class SignUpController {

    @Autowired
    private UserService userService;

    @Autowired
    private GenerateJWT generateJWT ;

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
                            singInCredientials.getHttpStatus()
                    ), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/enroll")
    public ResponseEntity<StandardResponse> enrollCourses(@RequestHeader("AuthenticationHeader") String accessToken , @RequestBody EnrollRequestDTO enrollRequestDTO ){
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

    @PostMapping("/view_results/{email}")
    public ResponseEntity<StandardResponse> viewResults(@RequestHeader("AuthenticationHeader") String accessToken , @PathVariable("email") String email ){
        if(generateJWT.validateToken(accessToken).isAuthenticationStatus()){
            return  userService.viewResults(email);
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
