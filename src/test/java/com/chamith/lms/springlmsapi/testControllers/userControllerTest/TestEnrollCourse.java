package com.chamith.lms.springlmsapi.testControllers.userControllerTest;

import com.chamith.lms.springlmsapi.controller.AdminController;
import com.chamith.lms.springlmsapi.controller.UserController;
import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
import com.chamith.lms.springlmsapi.service.AdminService;
import com.chamith.lms.springlmsapi.service.UserService;
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
public class TestEnrollCourse {
    @Mock
    private GenerateJWT generateJWT;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    private String validAdminToken;
    private String invalidToken;
    private String validNonAdminToken;
    private EnrollRequestDTO enrollRequestDTO ;

    @Before
    public void setup() {
        validAdminToken = "valid_admin_token";
        invalidToken = "invalid_token";
        validNonAdminToken = "valid_non_admin_token";
        enrollRequestDTO = new EnrollRequestDTO("Subject");
    }

    @Test
    public void testUpdateResultSuccess() {
        when(generateJWT.validateToken(validNonAdminToken)).
                thenReturn(new AuthenticationVerification(true ));
        when(generateJWT.extractSubject(validNonAdminToken)).thenReturn("test@gmail.com");
        when(userService.enroll(enrollRequestDTO, "test@gmail.com")).thenReturn(
                new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Enrolled course = "+enrollRequestDTO.getSubject(),
                                "Enrolled successfully  !!!!"
                        ), HttpStatus.OK));

        ResponseEntity<StandardResponse> response = userController.enrollCourses(validNonAdminToken , enrollRequestDTO);

        verify(generateJWT, times(1)).validateToken(validNonAdminToken);
        verify(userService).enroll(enrollRequestDTO, "test@gmail.com");
        verify(generateJWT).extractSubject(validNonAdminToken);
        assertResponseStatus(response, HttpStatus.OK,"Enrolled course = "+enrollRequestDTO.getSubject(),"Enrolled successfully  !!!!");
    }

    @Test
    public void testUpdateResultUnauthorized() {
        when(generateJWT.validateToken(invalidToken)).
                thenReturn(new AuthenticationVerification(false ));

        ResponseEntity<StandardResponse> response = userController.enrollCourses(invalidToken , enrollRequestDTO);

        verify(generateJWT, times(1)).validateToken(invalidToken);
        assertResponseStatus(response, HttpStatus.UNAUTHORIZED,"Unauthorized Access","Sign in failed !!!!");
    }

    private void assertResponseStatus(ResponseEntity<StandardResponse> response, HttpStatus expectedStatus , String msg , String data) {
        assert response.getStatusCode().equals(expectedStatus);
        assert response.getBody().getMessage().equals(msg);
        assert response.getBody().getData().equals(data);
    }
}
