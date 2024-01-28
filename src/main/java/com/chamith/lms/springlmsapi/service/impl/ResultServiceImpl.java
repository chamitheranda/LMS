package com.chamith.lms.springlmsapi.service.impl;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import com.chamith.lms.springlmsapi.dto.responseDTO.ViewResultsResponseDTO;
import com.chamith.lms.springlmsapi.mappers.ResultMapper;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.ResultService;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    private static final Logger logger = LoggerFactory.getLogger(ResultServiceImpl.class);

    @Autowired
    private ResultMapper resultMapper ;

    @Autowired
    private UserMapper userMapper ;

    @Override
    public ResponseEntity<StandardResponse> addResult(ResultsRequestDTO resultsRequestDTO, String email) {
        if(userMapper.doesEmailExist(email) ){
            if(userMapper.getPrivilegeLevel(email).equals("admin")){
                logger.warn("Admin user email = "+email+" doesn't have results");
                return new ResponseEntity<>(
                        new StandardResponse(
                                417,
                                "User is a admin",
                                "Admin doesn't have results !!!!"
                        ), HttpStatus.EXPECTATION_FAILED);
            }else {
                if(!resultMapper.doesSubjectExist(resultsRequestDTO.getSubject())){
                   if(userMapper.doesSubjectExist(resultsRequestDTO.getSubject() , email)){
                       resultMapper.addResults(resultsRequestDTO);
                       logger.info("Results added successfully subject = "+resultsRequestDTO.getSubject());
                       return new ResponseEntity<>(
                               new StandardResponse(
                                       200,
                                       "Results entered",
                                       "Result enter successfully !!!!"
                               ), HttpStatus.OK);
                   }else {
                       logger.error("User "+email+" doesn't enroll course = "+resultsRequestDTO.getSubject());
                       return new ResponseEntity<>(
                               new StandardResponse(
                                       417,
                                       "User is not enrolled course = "+resultsRequestDTO.getSubject(),
                                       "Result adding failed !!!!"
                               ), HttpStatus.EXPECTATION_FAILED);
                   }
                }else {
                    logger.warn("Result already entered for subject : "
                            +resultsRequestDTO.getSubject() + " for user email = "+email);
                    return new ResponseEntity<>(
                            new StandardResponse(
                                    208,
                                    "Results exists",
                                    "Result already entered  !!!!"
                            ), HttpStatus.ALREADY_REPORTED);
                }
            }

        }else {
            logger.warn("Email not found");
            return new ResponseEntity<>(
                    new StandardResponse(
                            204,
                            "email not found",
                            "Results Update failed !!!!"
                    ), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<StandardResponse> viewResults(String email) {
        if(userMapper.doesEmailExist(email)){
            List<ViewResultsResponseDTO> resultSet = userMapper.getAllResults(email);
            logger.info("User : "+email+" Viewed Results");
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "This is the results ",
                            resultSet
                    ), HttpStatus.OK);
        }else {
            logger.info("Email not found");
            return new ResponseEntity<>(
                    new StandardResponse(
                            204,
                            "No Results for = " + email ,
                            "Results Not Found  !!!!"
                    ), HttpStatus.NOT_FOUND);
        }
    }
}
