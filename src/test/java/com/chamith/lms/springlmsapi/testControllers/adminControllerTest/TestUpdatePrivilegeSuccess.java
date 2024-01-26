package com.chamith.lms.springlmsapi.testControllers;

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
public class TestUpdatePrivilegeSuccess {

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
        // Set up test data
        validAdminToken = "valid_admin_token";
        invalidToken = "invalid_token";
        validNonAdminToken = "valid_non_admin_token";
    }

    @Test
    public void testUpdatePrivilegeSuccess() {
        // Scenario 1: Successful Update Privilege

        // Mocking token validation
        when(generateJWT.validateToken(validAdminToken)).thenReturn(new AuthenticationVerification(true , "admin"));

        // Mocking token extraction
        when(generateJWT.extractSubject(validAdminToken)).thenReturn("admin@example.com");

        // Mocking service behavior
        when(adminService.updatePrivilege("admin@example.com")).thenReturn(
                new ResponseEntity<>(new StandardResponse(200, "Update Privilege", "Update successfully !!!!"), HttpStatus.OK));

        // Perform the test
        ResponseEntity<StandardResponse> response = adminController.updatePrivilege(validAdminToken);

        // Assertions
        verify(generateJWT, times(1)).validateToken(validAdminToken);
        verify(generateJWT).extractSubject(validAdminToken);
        verify(adminService).updatePrivilege("admin@example.com");
        assertResponseStatus(response, HttpStatus.OK);
        // Add more assertions if needed
    }

    @Test
    public void testUpdatePrivilegeUnauthorizedAccess() {
        // Scenario 2: Unauthorized Access (Invalid Token)

        // Mocking token validation
        when(generateJWT.validateToken(invalidToken)).thenReturn(new AuthenticationVerification(false));

        // Perform the test
        ResponseEntity<StandardResponse> response = adminController.updatePrivilege(invalidToken);

        // Assertions
        verify(generateJWT).validateToken(invalidToken);
        assertResponseStatus(response, HttpStatus.UNAUTHORIZED);
        // Add more assertions if needed
    }

    @Test
    public void testUpdatePrivilegeAccessDenied() {
        // Scenario 3: Access Denied (Non-Admin User)

        // Mocking token validation
        when(generateJWT.validateToken(validNonAdminToken)).thenReturn(new AuthenticationVerification(true , "none"));

        // Perform the test
        ResponseEntity<StandardResponse> response = adminController.updatePrivilege(validNonAdminToken);

        // Assertions
        verify(generateJWT).validateToken(validNonAdminToken);
        assertResponseStatus(response, HttpStatus.FORBIDDEN);
        // Add more assertions if needed
    }

    private void assertResponseStatus(ResponseEntity<StandardResponse> response, HttpStatus expectedStatus) {
        assert response.getStatusCode().equals(expectedStatus);
    }
}
