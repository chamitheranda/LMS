package com.chamith.lms.springlmsapi.testControllers.authControllerTest;

import com.chamith.lms.springlmsapi.controller.AuthController;
import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.service.AuthService;
import com.chamith.lms.springlmsapi.util.SingInCredientials;
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
public class TestSignIn {
    @InjectMocks
    private AuthController authController ;

    @Mock
    private AuthService authService;

    private SingInCredientials singInCredientials ;
    private SignInRequestDTO signInRequestDTO ;

    @Before
    public void setUp(){
        signInRequestDTO = new SignInRequestDTO(
                "email",
                "passWord"
        );

        singInCredientials = new SingInCredientials(
                "token",
                HttpStatus.OK
        );
    }

    @Test
    public void testSignInSucess(){
        when(authService.signIn(signInRequestDTO)).thenReturn(singInCredientials);

        ResponseEntity<StandardResponse> response = authController.signIn(signInRequestDTO);

        verify(authService).signIn(signInRequestDTO);

        assertResponseStatus(
                response,
                HttpStatus.OK,
                "Successfully sign in !!!!",
                singInCredientials.getToken()
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
