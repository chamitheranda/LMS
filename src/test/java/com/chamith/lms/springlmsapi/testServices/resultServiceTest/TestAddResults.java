package com.chamith.lms.springlmsapi.testServices.resultServiceTest;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import com.chamith.lms.springlmsapi.mappers.ResultMapper;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
import com.chamith.lms.springlmsapi.service.ResultService;
import com.chamith.lms.springlmsapi.service.impl.ResultServiceImpl;
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
public class TestAddResults {
    @InjectMocks
    private ResultServiceImpl resultService ;

    @Mock
    private UserMapper userMapper ;

    @Mock
    private ResultMapper resultMapper ;

    private String accessToken ;
    private String email ;
    private ResultsRequestDTO resultsRequestDTO ;

    @Before
    public void setUp(){
        accessToken = "token";
        email = "example@gmail.com" ;
        resultsRequestDTO = new ResultsRequestDTO(
                "name",
                "subject",
                "results"
        );
    }

    @Test
    public void testAddResultsNotfound(){
        when(userMapper.doesEmailExist(email)).thenReturn(false);

        ResponseEntity<StandardResponse> response = resultService.addResult(resultsRequestDTO,email);

        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.NOT_FOUND,
                "email not found",
                "Results Update failed !!!!"
        );
    }


    @Test
    public void testAddResultsExpectationFailed(){
        when(userMapper.doesEmailExist(email)).thenReturn(true);
        when(userMapper.getPrivilegeLevel(email)).thenReturn("admin");

        ResponseEntity<StandardResponse> response = resultService.addResult(resultsRequestDTO,email);

        verify(userMapper).doesEmailExist(email);
        verify(userMapper).getPrivilegeLevel(email);

        assertResponseStatus(
                response,
                HttpStatus.EXPECTATION_FAILED,
                "User is a admin",
                "Admin doesn't have results !!!!"
        );
    }

    @Test
    public void testAddResultsAlreadyReported(){
        when(userMapper.doesEmailExist(email)).thenReturn(true);
        when(userMapper.getPrivilegeLevel(email)).thenReturn("none");
        when(resultMapper.doesSubjectExist(resultsRequestDTO.getSubject())).thenReturn(true);

        ResponseEntity<StandardResponse> response = resultService.addResult(resultsRequestDTO,email);

        verify(userMapper).doesEmailExist(email);
        verify(userMapper).getPrivilegeLevel(email);
        verify(resultMapper).doesSubjectExist(resultsRequestDTO.getSubject());

        assertResponseStatus(
                response,
                HttpStatus.ALREADY_REPORTED,
                "Results exists",
                "Result already entered  !!!!"
        );
    }

    @Test
    public void testAddResultsSucess(){
        when(userMapper.doesEmailExist(email)).thenReturn(true);
        when(userMapper.getPrivilegeLevel(email)).thenReturn("none");
        when(resultMapper.doesSubjectExist(resultsRequestDTO.getSubject())).thenReturn(false);
        when(userMapper.doesSubjectExist(resultsRequestDTO.getSubject() ,email)).thenReturn(true);

        ResponseEntity<StandardResponse> response = resultService.addResult(resultsRequestDTO,email);

        verify(userMapper).doesEmailExist(email);
        verify(userMapper).getPrivilegeLevel(email);
        verify(resultMapper).doesSubjectExist(resultsRequestDTO.getSubject());
        verify(userMapper).doesSubjectExist(resultsRequestDTO.getSubject(), email);


        assertResponseStatus(
                response,
                HttpStatus.OK,
                "Results entered",
                "Result enter successfully !!!!"
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
