package com.chamith.lms.springlmsapi.testServices;

import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.AdminService;
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

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestUpdatePrivilegeSuccess {
    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    @Mock
    private UserMapper userMapper;

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
    public void testUpdatePrivilege_SuccessfulUpdate() {

        when(userMapper.doesEmailExist(validNonAdminEmail)).thenReturn(true);
        when(userMapper.getPrivilegeLevel(validNonAdminEmail)).thenReturn("user");

        ResponseEntity<StandardResponse> response = adminServiceImpl.updatePrivilege(validNonAdminEmail);

        verify(userMapper).doesEmailExist(validNonAdminEmail);
        verify(userMapper).updatePrivilegeLevel(validNonAdminEmail);

        assertResponseStatus(response, HttpStatus.OK);
    }

    @Test
    public void testUpdatePrivilege_UserIsAdmin() {

        when(userMapper.doesEmailExist(adminEmail)).thenReturn(true);
        when(userMapper.getPrivilegeLevel(adminEmail)).thenReturn("admin");

        ResponseEntity<StandardResponse> response = adminServiceImpl.updatePrivilege(adminEmail);

        verify(userMapper).doesEmailExist(adminEmail);

        assertResponseStatus(response, HttpStatus.EXPECTATION_FAILED);
    }

    @Test
    public void testUpdatePrivilege_EmailNotFound() {
        when(userMapper.doesEmailExist(invalidEmail)).thenReturn(false);

        ResponseEntity<StandardResponse> response = adminServiceImpl.updatePrivilege(invalidEmail);

        verify(userMapper).doesEmailExist(invalidEmail);

        assertResponseStatus(response, HttpStatus.NOT_FOUND);
    }

    private void assertResponseStatus(ResponseEntity<StandardResponse> response, HttpStatus expectedStatus) {
        assert response.getStatusCode().equals(expectedStatus);
    }
}
