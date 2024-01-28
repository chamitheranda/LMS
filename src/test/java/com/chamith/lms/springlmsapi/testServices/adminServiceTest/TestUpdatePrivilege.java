package com.chamith.lms.springlmsapi.testServices.adminServiceTest;

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

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestUpdatePrivilege {

    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    @Mock
    private UserMapper userMapper;

    private String email;

    @Before
    public void setup() {
       email = "email";
    }

    @Test
    public void testUpdatePrivilegeSuccessful() {

        when(userMapper.getPrivilegeLevel(email)).thenReturn("none");
        when(userMapper.doesEmailExist(email)).thenReturn(true);

        ResponseEntity<StandardResponse> response = adminServiceImpl.updatePrivilege(email);

        verify(userMapper).getPrivilegeLevel(email);
        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.OK,
                "Update Privilege",
                "Update successfully !!!!"
        );

    }

    @Test
    public void testUpdatePrivilegeExpectationFailed() {

        when(userMapper.getPrivilegeLevel(email)).thenReturn("admin");
        when(userMapper.doesEmailExist(email)).thenReturn(true);

        ResponseEntity<StandardResponse> response = adminServiceImpl.updatePrivilege(email);

        verify(userMapper).getPrivilegeLevel(email);
        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.EXPECTATION_FAILED,
                "User is an admin email = "+email,
                "Update Failed !!!!"
        );

    }

    @Test
    public void testUpdatePrivilegeNotFound() {
        when(userMapper.doesEmailExist(email)).thenReturn(false);

        ResponseEntity<StandardResponse> response = adminServiceImpl.updatePrivilege(email);

        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.NOT_FOUND,
                "User not found ",
                "Privilege Update Failed !!!!"
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
