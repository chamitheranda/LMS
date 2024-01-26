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
public class TestUpdateResults {
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
    public void testUpdateResultSuccessful() {

        when(userMapper.doesEmailExist(validNonAdminEmail)).thenReturn(true);
        when(userMapper.getPrivilegeLevel(validNonAdminEmail)).thenReturn("user");

        ResponseEntity<StandardResponse> response = adminServiceImpl.updateResults(validNonAdminEmail,"A");

        verify(userMapper).doesEmailExist(validNonAdminEmail);
        verify(userMapper).getPrivilegeLevel(validNonAdminEmail);

        assertResponseStatus(response, HttpStatus.OK);
    }

    @Test
    public void testUpdateResultNotFound() {

        when(userMapper.doesEmailExist(validNonAdminEmail)).thenReturn(false);

        ResponseEntity<StandardResponse> response = adminServiceImpl.updateResults(validNonAdminEmail , "A");

        verify(userMapper).doesEmailExist(validNonAdminEmail);

        assertResponseStatus(response, HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateResultExpectationFailed() {
        when(userMapper.doesEmailExist(validNonAdminEmail)).thenReturn(true);
        when(userMapper.getPrivilegeLevel(validNonAdminEmail)).thenReturn("admin");

        ResponseEntity<StandardResponse> response = adminServiceImpl.updateResults(validNonAdminEmail,"A");

        verify(userMapper).doesEmailExist(validNonAdminEmail);
        verify(userMapper).getPrivilegeLevel(validNonAdminEmail);

        assertResponseStatus(response, HttpStatus.EXPECTATION_FAILED);
    }

    private void assertResponseStatus(ResponseEntity<StandardResponse> response, HttpStatus expectedStatus) {
        assert response.getStatusCode().equals(expectedStatus);
    }

}
