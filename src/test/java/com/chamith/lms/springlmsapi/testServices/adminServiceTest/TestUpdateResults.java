package com.chamith.lms.springlmsapi.testServices.adminServiceTest;

import com.chamith.lms.springlmsapi.mappers.EnrolledCourseMapper;
import com.chamith.lms.springlmsapi.mappers.ResultMapper;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.impl.AdminServiceImpl;
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
public class TestUpdateResults {
    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ResultMapper resultMapper ;

    @Mock
    private EnrolledCourseMapper enrolledCourseMapper ;

    private String email;

    @Before
    public void setup() {
       email = "email";
    }
    @Test
    public void testUpdateResultSuccessful() {

        when(userMapper.doesEmailExist(email)).thenReturn(true);

        ResponseEntity<StandardResponse> response = adminServiceImpl.updateResults(email,"A");

        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.OK ,
                "Update Results" ,
                "Result Update successfully !!!!"
        );
    }

    @Test
    public void testUpdateResultNotFound() {

        when(userMapper.doesEmailExist(email)).thenReturn(false);

        ResponseEntity<StandardResponse> response = adminServiceImpl.updateResults(email , "A");

        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.NOT_FOUND ,
                "email not found",
                "update failed !!!!"
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
