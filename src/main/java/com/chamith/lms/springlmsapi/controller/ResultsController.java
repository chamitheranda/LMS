package com.chamith.lms.springlmsapi.controller;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import com.chamith.lms.springlmsapi.service.ResultService;
import com.chamith.lms.springlmsapi.util.AuthenticationVerification;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/results")
public class ResultsController {

    @Autowired
    private GenerateJWT generateJWT ;

    @Autowired
    private ResultService resultService;

    private static final Logger logger = LoggerFactory.getLogger(ResultsController.class);

    @PostMapping("/add_results/{email}")
    public ResponseEntity<StandardResponse> addResults(@RequestHeader("AuthenticationHeader") String accessToken ,
                                                       @RequestBody ResultsRequestDTO resultsRequestDTO,
                                                       @PathVariable("email") final String email ){
        AuthenticationVerification authenticationVerification = generateJWT.validateToken(accessToken);
        if (authenticationVerification.isAuthenticationStatus()) {
            if (authenticationVerification.getPrivilegeLevel().equals("admin")) {
                return resultService.addResult(resultsRequestDTO, email);
            } else {
                logger.warn("Access Denied for user email = "+email);
                return new ResponseEntity<>(
                        new StandardResponse(
                                403,
                                "User hasn't access",
                                "Access denied !!!!"
                        ), HttpStatus.FORBIDDEN);
            }

        }else {
            logger.warn("Unauthorized Access");
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            "Sign in failed !!!!"
                    ), HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/view_results/{email}")
    public ResponseEntity<StandardResponse> viewResults(@RequestHeader("AuthenticationHeader") String accessToken ){
        if(generateJWT.validateToken(accessToken).isAuthenticationStatus()){
            String email = generateJWT.extractSubject(accessToken) ;
            return  resultService.viewResults(email);
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
