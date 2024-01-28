package com.chamith.lms.springlmsapi.controller;

import com.chamith.lms.springlmsapi.service.AdminService;
import com.chamith.lms.springlmsapi.service.impl.ResultServiceImpl;
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
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private GenerateJWT generateJWT;

    @Autowired
    private AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PatchMapping("/update_privilege/{email}")
    public ResponseEntity<StandardResponse> updatePrivilege(@RequestHeader("AuthenticationHeader") String accessToken ,
                                                            @PathVariable("email") final String email ) {
        AuthenticationVerification authenticationVerification = generateJWT.validateToken(accessToken);
        if (authenticationVerification.isAuthenticationStatus()) {
            if (authenticationVerification.getPrivilegeLevel().equals("admin")) {
                return adminService.updatePrivilege(email);
            } else {
                logger.warn("Access denied for user = " + email);
                return new ResponseEntity<>(
                        new StandardResponse(
                                403,
                                "User hasn't access | User is not a admin ",
                                "Access denied !!!!"
                        ), HttpStatus.FORBIDDEN);
            }

        } else {
            logger.warn("Unauthorized Access");
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            "Sign in failed !!!!"
                    ), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/delete_user/{email}")
    public ResponseEntity<StandardResponse> removeUser(@RequestHeader("AuthenticationHeader") String accessToken,
                                                       @PathVariable("email") final String email
    ) {
        AuthenticationVerification authenticationVerification = generateJWT.validateToken(accessToken);
        if (authenticationVerification.isAuthenticationStatus()) {
            if (authenticationVerification.getPrivilegeLevel().equals("admin")) {
                return adminService.deleteUser(email);
            } else {
                logger.warn("Access denied for user = " + email);
                return new ResponseEntity<>(
                        new StandardResponse(
                                403,
                                "User hasn't access | User is not a admin ",
                                "Access denied !!!!"
                        ), HttpStatus.FORBIDDEN);
            }

        } else {
            logger.warn("Unauthorized Access");
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            "Sign in failed !!!!"
                    ), HttpStatus.UNAUTHORIZED);
        }

    }

    @PatchMapping("/update_results/{email}/{result}/{subject}")
    public ResponseEntity<StandardResponse> updateResult(@RequestHeader("AuthenticationHeader") String accessToken,
                                                          @PathVariable("result") String result,
                                                          @PathVariable("email") String email,
                                                          @PathVariable("subject") String subject
                                                         ) {
        AuthenticationVerification authenticationVerification = generateJWT.validateToken(accessToken);
        if (authenticationVerification.isAuthenticationStatus()) {
            if (authenticationVerification.getPrivilegeLevel().equals("admin")) {
                return adminService.updateResults(email, result,subject);
            } else {
                logger.warn("Access denied for user = " + email);
                return new ResponseEntity<>(
                        new StandardResponse(
                                403,
                                "User hasn't access | User is not a admin ",
                                "Access denied !!!!"
                        ), HttpStatus.FORBIDDEN);
            }
        } else {
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