package com.chamith.lms.springlmsapi.testControllers.authControllerTest;

import com.chamith.lms.springlmsapi.controller.AuthController;
import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.service.AuthService;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestSignUp {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService ;

    private UserRequestDTO userRequestDTO ;
    private String msg ;

    @Before
    public void setUp(){
        msg = "User details registered";
        userRequestDTO = new UserRequestDTO(
                "name",
                "email",
                "contactNumber",
                "passWord"
        );
    }

    @Test
    public void testSignUpSucess(){
        when(authService.addUser(userRequestDTO)).thenReturn(msg);

        ResponseEntity<StandardResponse> response = authController.signUp(userRequestDTO);

        verify(authService).addUser(userRequestDTO);

        assertResponseStatus(
                response,
                HttpStatus.CREATED,
                "Successfully sign up",
                "User details registered"
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
