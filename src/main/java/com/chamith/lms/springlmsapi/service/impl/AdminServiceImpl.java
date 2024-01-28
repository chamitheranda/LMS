package com.chamith.lms.springlmsapi.service.impl;

import com.chamith.lms.springlmsapi.mappers.EnrolledCourseMapper;
import com.chamith.lms.springlmsapi.mappers.ResultMapper;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.AdminService;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public ResponseEntity<StandardResponse> updatePrivilege(String email) {
        if(userMapper.doesEmailExist(email)){
            if(userMapper.getPrivilegeLevel(email).equals("none") ){
                userMapper.updatePrivilegeLevel(email);
                logger.info("Update user privilege : "+email );
                return new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Update Privilege",
                                "Update successfully !!!!"
                        ), HttpStatus.OK);
            }else {
                logger.warn("Admin user can't Update , email = "+email );
                return new ResponseEntity<>(
                        new StandardResponse(
                                417,
                                "User is an admin email = "+email ,
                                "Update Failed !!!!"
                        ), HttpStatus.EXPECTATION_FAILED);
            }
        }else {
            logger.info("User Not found email = "+email + " or admin user ");
            return new ResponseEntity<>(
                    new StandardResponse(
                            204,
                            "User not found ",
                            "Privilege Update Failed !!!!"
                    ), HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public ResponseEntity<StandardResponse> deleteUser(String email) {
        if(userMapper.doesEmailExist(email)){
            if(userMapper.getPrivilegeLevel(email).equals("none") ){
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
                    logger.info("Delete User from Results by email : "+email);
                    return new ResponseEntity<>(
                            new StandardResponse(
                                    200,
                                    "User Delete",
                                    "Delete successfully !!!!"
                            ), HttpStatus.OK);
                }catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Internal Server Error");
                    return new ResponseEntity<>(
                            new StandardResponse(
                                    500,
                                    "Internal Server Error | Execution fallback",
                                    "Delete Failed: An unexpected error occurred !!!!"
                            ), HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }else {
                logger.warn("Admin user can't delete email = "+email );
                return new ResponseEntity<>(
                        new StandardResponse(
                                417,
                                "User is an admin email = "+email ,
                                "Delete Failed !!!!"
                        ), HttpStatus.EXPECTATION_FAILED);
            }
        }else{
            logger.info("User Not found email = "+email );
            return new ResponseEntity<>(
                    new StandardResponse(
                            204,
                            "User not found email = "+email,
                            "Delete Failed !!!!"
                    ), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<StandardResponse> updateResults(String email, String result, String subject) {
       if(userMapper.doesEmailExist(email)){
           if(userMapper.getPrivilegeLevel(email).equals("none") ){
               if(resultMapper.doesResultsExistByEmailAndSubject(email,subject)){
                   userMapper.updateResult(result);
                   logger.info("Result Update Successfully email : "+email);
                   return new ResponseEntity<>(
                           new StandardResponse(
                                   200,
                                   "Update Results",
                                   "Result Update successfully !!!!"
                           ), HttpStatus.OK);
               }else{
                   logger.warn("subject = "+subject+" not exist with email = "+email);
                   return new ResponseEntity<>(
                           new StandardResponse(
                                   204,
                                   "No Subject Present in DB",
                                   "Result Update Failed !!!!"
                           ), HttpStatus.NO_CONTENT);
               }
           }else {
               logger.warn("Admin user doesn't have results , email = "+email );
               return new ResponseEntity<>(
                       new StandardResponse(
                               417,
                               "User is an admin email = "+email ,
                               "Result Update Failed !!!!"
                       ), HttpStatus.EXPECTATION_FAILED);
           }
       }else {
           logger.info("User Not found email = "+email + " or admin user ");
           return new ResponseEntity<>(
                   new StandardResponse(
                           204,
                           "User not found ",
                           "Update Failed !!!!"
                   ), HttpStatus.NOT_FOUND);
       }
    }
}

