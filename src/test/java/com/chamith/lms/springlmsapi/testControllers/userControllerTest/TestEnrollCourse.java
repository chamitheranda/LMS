package com.chamith.lms.springlmsapi.testControllers.userControllerTest;

import com.chamith.lms.springlmsapi.controller.UserController;
import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
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


    private String accessToken;
    private EnrollRequestDTO enrollRequestDTO ;

    @Before
    public void setup() {
        accessToken = "accessToken";
        enrollRequestDTO = new EnrollRequestDTO("Subject");
    }

    @Test
    public void testUpdateResultSuccess() {
        when(generateJWT.validateToken(accessToken)).
                thenReturn(new AuthenticationVerification(true ));
        when(generateJWT.extractSubject(accessToken)).thenReturn("test@gmail.com");
        when(userService.enroll(enrollRequestDTO, "test@gmail.com")).thenReturn(
                new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Enrolled course = "+enrollRequestDTO.getSubject(),
                                "Enrolled successfully  !!!!"
                        ), HttpStatus.OK));

        ResponseEntity<StandardResponse> response = userController.enrollCourses(accessToken , enrollRequestDTO);

        verify(generateJWT, times(1)).validateToken(accessToken);
        verify(userService).enroll(enrollRequestDTO, "test@gmail.com");
        verify(generateJWT).extractSubject(accessToken);
        assertResponseStatus(
                response,
                HttpStatus.OK,
                "Enrolled course = "+enrollRequestDTO.getSubject(),
                "Enrolled successfully  !!!!");
    }

    @Test
    public void testUpdateResultUnauthorized() {
        when(generateJWT.validateToken(accessToken)).
                thenReturn(new AuthenticationVerification(false ));

        ResponseEntity<StandardResponse> response = userController.enrollCourses(accessToken , enrollRequestDTO);

        verify(generateJWT, times(1)).validateToken(accessToken);
        assertResponseStatus(
                response,
                HttpStatus.UNAUTHORIZED,
                "Unauthorized Access",
                "Sign in failed !!!!"
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
