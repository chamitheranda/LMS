package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import com.chamith.lms.springlmsapi.mappers.ResultMapper;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    @Autowired
    private ResultMapper resultMapper ;

    @Autowired
    private UserMapper userMapper ;

    public ResponseEntity<StandardResponse> addResult(ResultsRequestDTO resultsRequestDTO) {
        if(userMapper.doesEmailExist(resultsRequestDTO.getEmail()) ){
            if(userMapper.getPrivilegeLevel(resultsRequestDTO.getEmail()).equals("admin")){
                return new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "User is a admin",
                                "Admin doesn't have results !!!!"
                        ), HttpStatus.OK);
            }else {
                if(resultMapper.doesSubjectExist(resultsRequestDTO.getSubject())){
                    resultMapper.addResults(resultsRequestDTO);
                    return new ResponseEntity<>(
                            new StandardResponse(
                                    200,
                                    "Results entered",
                                    "Result enter successfully !!!!"
                            ), HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(
                            new StandardResponse(
                                    200,
                                    "Results exists",
                                    "Result already enter successfully !!!!"
                            ), HttpStatus.OK);
                }
            }

        }else {
            return new ResponseEntity<>(
                    new StandardResponse(
                            204,
                            "email not found",
                            "Results Update failed !!!!"
                    ), HttpStatus.NOT_FOUND);
        }
    }
}
