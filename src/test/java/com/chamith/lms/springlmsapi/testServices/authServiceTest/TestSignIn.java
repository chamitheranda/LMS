package com.chamith.lms.springlmsapi.testServices.authServiceTest;

import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.AuthService;
import com.chamith.lms.springlmsapi.service.impl.AuthServiceImpl;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestSignIn {
    @InjectMocks
    private AuthServiceImpl authService ;

    @Mock
    private GenerateJWT generateJWT ;

    @Mock
    private UserMapper userMapper ;

    @Mock
    private PasswordEncoder passwordEncoder ;

    private SignInRequestDTO signInRequestDTO ;
    private String password ;

    @Before
    public void setUp(){
        signInRequestDTO = new SignInRequestDTO(
                "email",
                "passWord"
        );

    }

    @Test
    public void testSignInSucess(){
        when(userMapper.selectPasswordByEmail(signInRequestDTO.getEmail())).thenReturn(password);
        when(passwordEncoder.matches(signInRequestDTO.getPassword(),password)).thenReturn(true);

        SingInCredientials response = authService.signIn(signInRequestDTO);

        verify(passwordEncoder).matches(signInRequestDTO.getPassword(),password);
        verify(userMapper).selectPasswordByEmail(signInRequestDTO.getEmail());

        assertResponseStatus(
                response,
                HttpStatus.OK

        );
    }

    @Test
    public void testSignInFailed(){
        when(userMapper.selectPasswordByEmail(signInRequestDTO.getEmail())).thenReturn(password);
        when(passwordEncoder.matches(signInRequestDTO.getPassword(),password)).thenReturn(false);

        SingInCredientials response = authService.signIn(signInRequestDTO);

        verify(passwordEncoder).matches(signInRequestDTO.getPassword(),password);
        verify(userMapper).selectPasswordByEmail(signInRequestDTO.getEmail());

        assertResponseStatus(
                response,
                HttpStatus.UNAUTHORIZED

        );
    }

    private void assertResponseStatus(
            SingInCredientials response,
            HttpStatus expectedStatus
    ) {
        assert response.getHttpStatus().equals(expectedStatus);
    }
}
