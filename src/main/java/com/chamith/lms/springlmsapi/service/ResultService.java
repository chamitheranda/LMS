package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import com.chamith.lms.springlmsapi.dto.responseDTO.ViewResultsResponseDTO;
import com.chamith.lms.springlmsapi.mappers.ResultMapper;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
                                417,
                                "User is a admin",
                                "Admin doesn't have results !!!!"
                        ), HttpStatus.EXPECTATION_FAILED);
            }else {
                if(!resultMapper.doesSubjectExist(resultsRequestDTO.getSubject())){
                   if(userMapper.doesSubjectExist(resultsRequestDTO.getSubject() , resultsRequestDTO.getEmail())){
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
                                       417,
                                       "User is not enrolled course = "+resultsRequestDTO.getSubject(),
                                       "Result adding failed !!!!"
                               ), HttpStatus.EXPECTATION_FAILED);
                   }
                }else {
                    return new ResponseEntity<>(
                            new StandardResponse(
                                    208,
                                    "Results exists",
                                    "Result already entered  !!!!"
                            ), HttpStatus.ALREADY_REPORTED);
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

    public ResponseEntity<StandardResponse> viewResults(String email) {
        if(userMapper.doesEmailExist(email)){
            List<ViewResultsResponseDTO> resultSet = userMapper.getAllResults(email);
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "This is the results ",
                            resultSet
                    ), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(
                    new StandardResponse(
                            204,
                            "No Results for = " + email ,
                            "Results Not Found  !!!!"
                    ), HttpStatus.NOT_FOUND);
        }
    }
}
