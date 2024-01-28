package com.chamith.lms.springlmsapi.testControllers.adminControllerTest;

import com.chamith.lms.springlmsapi.controller.AdminController;
import com.chamith.lms.springlmsapi.service.AdminService;
import com.chamith.lms.springlmsapi.util.AuthenticationVerification;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestUpdatePrivilege {

    @Mock
    private GenerateJWT generateJWT;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private String accessToken;
    private String userEmail;
    private String adminEmail;

    @Before
    public void setup() {
        accessToken = "accessToken";
        adminEmail = "adminEmail";
        userEmail = "userEmail";
    }

    @Test
    public void testUpdatePrivilegeSuccess() {

        when(generateJWT.validateToken(accessToken)).
                thenReturn(new AuthenticationVerification(true , "admin"));

        when(adminService.updatePrivilege(userEmail)).thenReturn(
                new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Update Privilege",
                                "Update successfully !!!!"),
                        HttpStatus.OK));

        ResponseEntity<StandardResponse> response = adminController.updatePrivilege(accessToken,userEmail);

        verify(generateJWT, times(1)).validateToken(accessToken);
        verify(adminService).updatePrivilege(userEmail);
        assertResponseStatus(
                response,
                HttpStatus.OK,
                "Update Privilege",
                "Update successfully !!!!"
                );
    }

    @Test
    public void testUpdatePrivilegeUnauthorizedAccess() {

        when(generateJWT.validateToken(accessToken)).thenReturn(new AuthenticationVerification(false));

        ResponseEntity<StandardResponse> response = adminController.updatePrivilege(accessToken,userEmail);

        verify(generateJWT).validateToken(accessToken);
        assertResponseStatus(
                response,
                HttpStatus.UNAUTHORIZED,
                "Unauthorized Access",
                "Sign in failed !!!!"
                );
    }

    @Test
    public void testUpdatePrivilegeAccessDenied() {

        when(generateJWT.validateToken(accessToken)).
                thenReturn(new AuthenticationVerification(true , "none"));

        ResponseEntity<StandardResponse> response = adminController.updatePrivilege(accessToken,userEmail);

        verify(generateJWT).validateToken(accessToken);
        assertResponseStatus(
                response,
                HttpStatus.FORBIDDEN,
                "User hasn't access | User is not a admin ",
                "Access denied !!!!"
                );
    }

    private void assertResponseStatus(
            ResponseEntity<StandardResponse> response,
            HttpStatus expectedStatus ,
            String msg ,
            Object data
    ) {
        assert response.getStatusCode().equals(expectedStatus);
        assert response.getBody().getMessage().equals(msg);
        assert response.getBody().getData().equals(data);
    }


}
