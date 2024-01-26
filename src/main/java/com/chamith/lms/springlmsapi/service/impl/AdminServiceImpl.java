package com.chamith.lms.springlmsapi.service.impl;

import com.chamith.lms.springlmsapi.mappers.EnrolledCourseMapper;
import com.chamith.lms.springlmsapi.mappers.ResultMapper;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.AdminService;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private ResultMapper resultMapper ;

    @Autowired
    private EnrolledCourseMapper enrolledCourseMapper ;

    @Override
    public ResponseEntity<StandardResponse> updatePrivilege(String email) {
        if(userMapper.doesEmailExist(email) ){
            if(userMapper.getPrivilegeLevel(email).equals("admin")){
                return new ResponseEntity<>(
                        new StandardResponse(
                                417,
                                "User is a admin",
                                "Already user is a admin !!!!"
                        ), HttpStatus.EXPECTATION_FAILED);
            }else {
                userMapper.updatePrivilegeLevel(email);
                return new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Update Privilege",
                                "Update successfully !!!!"
                        ), HttpStatus.OK);
            }

        }else {
                return new ResponseEntity<>(
                        new StandardResponse(
                                204,
                                "email not found",
                                "update failed !!!!"
                        ), HttpStatus.NOT_FOUND);
            }
        }
    @Override
    public ResponseEntity<StandardResponse> deleteUser(String email) {
        if(userMapper.doesEmailExist(email) ){
            if( userMapper.getPrivilegeLevel(email).equals("admin")){
                return new ResponseEntity<>(
                        new StandardResponse(
                                417,
                                "User is a admin",
                                "User can't delete !!!!"
                        ), HttpStatus.EXPECTATION_FAILED);
            }else {
                try{
                    if(enrolledCourseMapper.doesEmailExistEnrolledCourses(email)){
                        if(resultMapper.doesResultsExist(email)){
                            userMapper.deleteUserByEmailFromUsers(email);
                            enrolledCourseMapper.deleteUserFromEnrolledCourses(email);
                            resultMapper.deleteUserFromResults(email);
                        }else{
                            userMapper.deleteUserByEmailFromUsers(email);
                            enrolledCourseMapper.deleteUserFromEnrolledCourses(email);
                        }
                    }else {
                        userMapper.deleteUserByEmailFromUsers(email);
                    }

                    return new ResponseEntity<>(
                            new StandardResponse(
                                    200,
                                    "User Delete",
                                    "Delete successfully !!!!"
                            ), HttpStatus.OK);
                }catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(
                            new StandardResponse(
                                    500,
                                    "Internal Server Error | Execution fallback",
                                    "Delete Failed: An unexpected error occurred !!!!"
                            ), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

        }else {
            return new ResponseEntity<>(
                    new StandardResponse(
                            204,
                            "User not found",
                            "Delete Failed !!!!"
                    ), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<StandardResponse> updateResults(String email , String result) {
        if(userMapper.doesEmailExist(email) ){
            if(userMapper.getPrivilegeLevel(email).equals("admin")){
                return new ResponseEntity<>(
                        new StandardResponse(
                                417,
                                "User is a admin",
                                "Already user is a admin !!!!"
                        ), HttpStatus.EXPECTATION_FAILED);
            }else {
                userMapper.updateResult(result);
                return new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Update Results",
                                "Result Update successfully !!!!"
                        ), HttpStatus.OK);
            }

        }else {
            return new ResponseEntity<>(
                    new StandardResponse(
                            204,
                            "email not found",
                            "update failed !!!!"
                    ), HttpStatus.NOT_FOUND);
        }
    }
}

