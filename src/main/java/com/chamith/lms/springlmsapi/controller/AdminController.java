package com.chamith.lms.springlmsapi.controller;

import com.chamith.lms.springlmsapi.service.AdminService;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.StandardResponse;
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

    @PatchMapping("/update_privilege/{email}")
    public ResponseEntity<StandardResponse> updatePrivilege(@RequestHeader("AuthenticationHeader") String accessToken) {
        if (generateJWT.validateToken(accessToken).isAuthenticationStatus()) {
            String email = generateJWT.extractSubject(accessToken) ;
            if (generateJWT.validateToken(accessToken).getPrivilegeLevel().equals("admin")) {
                return adminService.updatePrivilege(email);
            } else {
                return new ResponseEntity<>(
                        new StandardResponse(
                                403,
                                "User hasn't access | User is not a admin ",
                                "Access denied !!!!"
                        ), HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            "Sign in failed !!!!"
                    ), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/delete_user/{email}")
    public ResponseEntity<StandardResponse> removeUser(@RequestHeader("AuthenticationHeader") String accessToken ) {
        if (generateJWT.validateToken(accessToken).isAuthenticationStatus()) {
            String email = generateJWT.extractSubject(accessToken) ;
            if (generateJWT.validateToken(accessToken).getPrivilegeLevel().equals("admin")) {
                return adminService.deleteUser(email);
            } else {
                return new ResponseEntity<>(
                        new StandardResponse(
                                403,
                                "User hasn't access | User is not a admin ",
                                "Access denied !!!!"
                        ), HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            "Sign in failed !!!!"
                    ), HttpStatus.UNAUTHORIZED);
        }

    }

    @PatchMapping("/update_results/{email}/{result}")
    public ResponseEntity<StandardResponse> updateResults(@RequestHeader("AuthenticationHeader") String accessToken,
                                                          @PathVariable("result") String result) {
        if (generateJWT.validateToken(accessToken).isAuthenticationStatus()) {
            String email = generateJWT.extractSubject(accessToken) ;
            if (generateJWT.validateToken(accessToken).getPrivilegeLevel().equals("admin")) {
                return adminService.updateResults(email, result);
            } else {
                return new ResponseEntity<>(
                        new StandardResponse(
                                403,
                                "User hasn't access | User is not a admin ",
                                "Access denied !!!!"
                        ), HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>(
                    new StandardResponse(
                            401,
                            "Unauthorized Access",
                            "Sign in failed !!!!"
                    ), HttpStatus.UNAUTHORIZED);
        }
    }
}
