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
public class TestDeleteUser {
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
    public void testDeleteUserSuccessful() {

        when(userMapper.getPrivilegeLevel(email)).thenReturn("none");
        when(userMapper.doesEmailExist(email)).thenReturn(true);

        ResponseEntity<StandardResponse> response = adminServiceImpl.deleteUser(email);

        verify(userMapper).getPrivilegeLevel(email);
        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.OK ,
                "User Delete" ,
                "Delete successfully !!!!"
        );
    }

    @Test
    public void testDeleteUserExpectationFailed() {

        when(userMapper.getPrivilegeLevel(email)).thenReturn("admin");
        when(userMapper.doesEmailExist(email)).thenReturn(true);

        ResponseEntity<StandardResponse> response = adminServiceImpl.deleteUser(email);

        verify(userMapper).getPrivilegeLevel(email);
        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.EXPECTATION_FAILED ,
                "User is an admin email = "+email ,
                "Delete Failed !!!!"
        );
    }

    @Test
    public void testDeleteUserNotFound() {

        when(userMapper.doesEmailExist(email)).thenReturn(false);

        ResponseEntity<StandardResponse> response = adminServiceImpl.deleteUser(email);

        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.NOT_FOUND ,
                "User not found email = "+email ,
                "Delete Failed !!!!"
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
