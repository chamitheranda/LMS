package com.chamith.lms.springlmsapi.testServices;

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

    private String adminEmail;
    private String invalidEmail;
    private String validNonAdminEmail;

    @Before
    public void setup() {
        adminEmail = "valid_admin_email";
        invalidEmail = "invalid_email";
        validNonAdminEmail = "valid_non_admin_email";
    }
    @Test
    public void testDeleteUserSuccessful() {

        when(userMapper.doesEmailExist(validNonAdminEmail)).thenReturn(true);
        when(resultMapper.doesResultsExist(validNonAdminEmail)).thenReturn(true);
        when(enrolledCourseMapper.doesEmailExistEnrolledCourses(validNonAdminEmail)).thenReturn(true);

        ResponseEntity<StandardResponse> response = adminServiceImpl.deleteUser(validNonAdminEmail);

        verify(userMapper).doesEmailExist(validNonAdminEmail);
        verify(resultMapper).doesResultsExist(validNonAdminEmail);
        verify(enrolledCourseMapper).doesEmailExistEnrolledCourses(validNonAdminEmail);

        assertResponseStatus(response, HttpStatus.OK , "User Delete" , "Delete successfully !!!!");

    }

    @Test
    public void testDeleteUserNotFound() {

        when(userMapper.doesEmailExist(validNonAdminEmail)).thenReturn(false);

        ResponseEntity<StandardResponse> response = adminServiceImpl.deleteUser(validNonAdminEmail);

        verify(userMapper).doesEmailExist(validNonAdminEmail);

        assertResponseStatus(response, HttpStatus.NOT_FOUND ,"User not found" , "Delete Failed !!!!" );

    }

    private void assertResponseStatus(ResponseEntity<StandardResponse> response, HttpStatus expectedStatus , String msg , String data) {
        assert response.getStatusCode().equals(expectedStatus);
        assert response.getBody().getMessage().equals(msg);
        assert response.getBody().getData().equals(data);
    }

}
