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
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class TestDeleteUser {

    @Mock
    private GenerateJWT generateJWT;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private String adminEmail;
    private String userEmail;
    private String accessToken;

    @Before
    public void setup() {
        adminEmail = "adminEmail";
        userEmail = "userEmail";
        accessToken = "token";
    }


    @Test
    public void testRemoveUserSuccess() {
        when(generateJWT.validateToken(accessToken)).
                thenReturn(new AuthenticationVerification(true , "admin"));

        when(adminService.deleteUser(userEmail)).thenReturn(
                new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "User Delete",
                                "Delete successfully !!!!"
                        ), HttpStatus.OK));

        ResponseEntity<StandardResponse> response = adminController.removeUser(accessToken ,userEmail);

        verify(generateJWT, times(1)).validateToken(accessToken);
        verify(adminService).deleteUser(userEmail);
        assertResponseStatus(
                response,
                HttpStatus.OK,
                "User Delete",
                "Delete successfully !!!!"
        );
    }

    @Test
    public void testRemoveUserAccessDenied() {
        when(generateJWT.validateToken(accessToken)).thenReturn(new AuthenticationVerification(false));

        ResponseEntity<StandardResponse> response = adminController.removeUser(accessToken,userEmail);

        verify(generateJWT).validateToken(accessToken);
        assertResponseStatus(
                response,
                HttpStatus.UNAUTHORIZED,
                "Unauthorized Access",
                "Sign in failed !!!!"
        );
    }

    @Test
    public void testRemoveUserUnauthorized() {
        when(generateJWT.validateToken(accessToken)).
                thenReturn(new AuthenticationVerification(true , "none"));

        ResponseEntity<StandardResponse> response = adminController.removeUser(accessToken,userEmail);

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
