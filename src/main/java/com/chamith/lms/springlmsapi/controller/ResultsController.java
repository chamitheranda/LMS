package com.chamith.lms.springlmsapi.controller;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import com.chamith.lms.springlmsapi.service.ResultService;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.StandardResponse;
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
    private ResultService resultService ;

    @PostMapping("/add_results")
    public ResponseEntity<StandardResponse> addResult(@RequestHeader("AuthenticationHeader") String accessToken , @RequestBody ResultsRequestDTO resultsRequestDTO){
        if(generateJWT.validateToken(accessToken).isAuthenticationStatus() && generateJWT.validateToken(accessToken).getPrivilegeLevel().equals("admin")){
            return  resultService.addResult(resultsRequestDTO);
        }else{
            return new ResponseEntity<>(
                    new StandardResponse(
                            403,
                            "User hasn't access",
                            "Access denied !!!!"
                    ), HttpStatus.FORBIDDEN);
        }

    }
}
