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

    private String validAdminToken;
    private String invalidToken;
    private String validNonAdminToken;

    @Before
    public void setup() {
        validAdminToken = "valid_admin_token";
        invalidToken = "invalid_token";
        validNonAdminToken = "valid_non_admin_token";
    }


    @Test
    public void testUpdateResultSuccess() {
        when(generateJWT.validateToken(validAdminToken)).
                thenReturn(new AuthenticationVerification(true , "admin"));

        when(adminService.updateResults("validAdmin@example.com", "A")).thenReturn(
                new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Update Results",
                                "Result Update successfully !!!!"
                        ), HttpStatus.OK));

        ResponseEntity<StandardResponse> response = adminController.updateResult(validAdminToken , "A" , "validAdmin@example.com");

        verify(generateJWT, times(1)).validateToken(validAdminToken);
        verify(adminService).updateResults("validAdmin@example.com","A");
        assertResponseStatus(response, HttpStatus.OK);
    }


    @Test
    public void testUpdateResultsAccessDenied() {
        when(generateJWT.validateToken(invalidToken)).thenReturn(new AuthenticationVerification(false));

        ResponseEntity<StandardResponse> response = adminController.updateResult(invalidToken , "A" , "dummy@gmail.com");

        verify(generateJWT).validateToken(invalidToken);
        assertResponseStatus(response, HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testRemoveUserUnauthorized() {
        when(generateJWT.validateToken(validNonAdminToken)).
                thenReturn(new AuthenticationVerification(true , "none"));

        ResponseEntity<StandardResponse> response = adminController.updateResult(validNonAdminToken , "A" , "dummy@gmail.com");

        verify(generateJWT).validateToken(validNonAdminToken);
        assertResponseStatus(response, HttpStatus.FORBIDDEN);
    }
    private void assertResponseStatus(ResponseEntity<StandardResponse> response, HttpStatus expectedStatus) {
        assert response.getStatusCode().equals(expectedStatus);
    }
}
