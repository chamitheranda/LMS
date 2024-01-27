package com.chamith.lms.springlmsapi.testServices.userServiceTest;

import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.impl.UserServiceImpl;
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
public class TestEnrollCourse {
    @Mock
    private UserMapper userMapper ;

    @InjectMocks
    private UserServiceImpl userService ;

    private String email ;
    private EnrollRequestDTO enrollRequestDTO ;

    @Before
    public void setUp(){
        email = "eample@gmail.com";
        enrollRequestDTO = new EnrollRequestDTO("subject");
    }

    @Test
    public  void TestEnrollSuccessWhenSubjectDoesNotExist(){
        when(userMapper.doesSubjectExist(email , enrollRequestDTO.getSubject())).thenReturn(false);

        ResponseEntity<StandardResponse> response = userService.enroll(enrollRequestDTO,email);

        verify(userMapper).doesSubjectExist(email , enrollRequestDTO.getSubject());

        assertResponseStatus(
                response,
                HttpStatus.OK,
                "Enrolled course = "+enrollRequestDTO.getSubject() ,
                "Enrolled successfully  !!!!"
        );

    }

    @Test
    public  void TestEnrollSuccessWhenSubjectExistEmailDoesNotExist(){
        when(userMapper.doesSubjectExist(email , enrollRequestDTO.getSubject())).thenReturn(true);
        when(userMapper.doesEmailExist(email)).thenReturn(false);
        ResponseEntity<StandardResponse> response = userService.enroll(enrollRequestDTO,email);

        verify(userMapper).doesSubjectExist(email , enrollRequestDTO.getSubject());
        verify(userMapper).doesEmailExist(email);
        assertResponseStatus(
                response,
                HttpStatus.OK,
                "Enrolled course = "+enrollRequestDTO.getSubject() ,
                "Enrolled successfully  !!!!"
        );

    }

    @Test
    public  void TestEnrollExpectationFailed(){
        when(userMapper.doesSubjectExist(email , enrollRequestDTO.getSubject())).thenReturn(true);
        when(userMapper.doesEmailExist(email)).thenReturn(true);
        ResponseEntity<StandardResponse> response = userService.enroll(enrollRequestDTO,email);

        verify(userMapper).doesSubjectExist(email , enrollRequestDTO.getSubject());
        verify(userMapper).doesEmailExist(email);
        assertResponseStatus(
                response,
                HttpStatus.EXPECTATION_FAILED,
                "User Enrolled subject =  "+enrollRequestDTO.getSubject(),
                "Already enrolled for this subject!!!!"
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
