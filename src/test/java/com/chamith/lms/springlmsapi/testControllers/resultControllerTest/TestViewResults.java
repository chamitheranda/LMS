package com.chamith.lms.springlmsapi.testControllers.resultControllerTest;

import com.chamith.lms.springlmsapi.controller.ResultsController;
import com.chamith.lms.springlmsapi.dto.responseDTO.ViewResultsResponseDTO;
import com.chamith.lms.springlmsapi.service.ResultService;
import com.chamith.lms.springlmsapi.util.AuthenticationVerification;
import com.chamith.lms.springlmsapi.util.GenerateJWT;
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
public class TestViewResults {
    @Mock
    private GenerateJWT generateJWT ;

    @Mock
    private ResultService resultService ;

    @InjectMocks
    private ResultsController resultsController ;

    private String accessToken ;
    private String email ;
    private ViewResultsResponseDTO results ;

    @Before
    public void setUp(){
        accessToken = "accessToken";
        email = "email";
        results = new ViewResultsResponseDTO(
                "subject",
                "results"
        );
    }
    @Test
    public void TestViewResults(){
        when(generateJWT.validateToken(accessToken)).thenReturn(new AuthenticationVerification(true));
        when(generateJWT.extractSubject(accessToken)).thenReturn(email);
        when(resultService.viewResults(email)).thenReturn(
                new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "This is the results ",
                                results
                        ), HttpStatus.OK));
        ResponseEntity<StandardResponse> response = resultsController.viewResults(accessToken);

        verify(generateJWT).validateToken(accessToken);
        verify(generateJWT).extractSubject(accessToken);
        verify(resultService).viewResults(email);
        assertResponseStatus(
                response,
                HttpStatus.OK,
                "This is the results ",
                results
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
