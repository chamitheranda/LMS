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
    private GenerateJWT generateJWT ;

    @Autowired
    private AdminService adminService ;

    @PatchMapping("/update_privilege/{email}")
    public ResponseEntity<StandardResponse> updatePrivilege(@RequestHeader("AuthenticationHeader") String accessToken ,@PathVariable("email") String email){
        if(generateJWT.validateToken(accessToken).isAuthenticationStatus() && generateJWT.validateToken(accessToken).getPrivilegeLevel().equals("admin")){
            String msg = adminService.updatePrivilege(email);
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "Update Privilege",
                            msg
                    ), HttpStatus.OK);
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