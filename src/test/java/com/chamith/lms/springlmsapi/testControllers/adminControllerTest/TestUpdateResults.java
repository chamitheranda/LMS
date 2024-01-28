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
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestUpdateResults {
    @Mock
    private GenerateJWT generateJWT;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private String accessToken;

    @Before
    public void setup() {
       accessToken = "accessToken";
    }

    @Test
    public void testUpdateResultSuccess() {
        when(generateJWT.validateToken(accessToken)).
                thenReturn(new AuthenticationVerification(true , "admin"));

        when(adminService.updateResults("validAdmin@example.com", "A", "subject")).thenReturn(
                new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Update Results",
                                "Result Update successfully !!!!"
                        ), HttpStatus.OK));

        ResponseEntity<StandardResponse> response = adminController.updateResult(
                accessToken ,
                "A" ,
                "validAdmin@example.com",
                "subject"
        );

        verify(generateJWT, times(1)).validateToken(accessToken);
        verify(adminService).updateResults("validAdmin@example.com","A", "subject");
        assertResponseStatus(
                response,
                HttpStatus.OK,
                "Update Results",
                "Result Update successfully !!!!"
                );
    }


    @Test
    public void testUpdateResultsAccessDenied() {
        when(generateJWT.validateToken(accessToken)).thenReturn(new AuthenticationVerification(false));

        ResponseEntity<StandardResponse> response = adminController.updateResult(accessToken , "A" , "dummy@gmail.com","subject");

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

        ResponseEntity<StandardResponse> response = adminController.updateResult(
                accessToken ,
                "A" ,
                "dummy@gmail.com",
                "subject"
        );

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
