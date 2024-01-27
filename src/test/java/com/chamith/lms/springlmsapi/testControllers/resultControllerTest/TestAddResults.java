package com.chamith.lms.springlmsapi.testControllers.resultControllerTest;

import com.chamith.lms.springlmsapi.controller.ResultsController;
import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
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
public class TestAddResults {
    @InjectMocks
    private ResultsController resultsController ;

    @Mock
    private GenerateJWT generateJWT ;

    @Mock
    private ResultService resultService ;

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
    public void testAddResultSuccess(){
        when(generateJWT.validateToken(accessToken)).thenReturn(new AuthenticationVerification(true , "admin"));
        when(generateJWT.extractSubject(accessToken)).thenReturn(email);
        when(resultService.addResult(resultsRequestDTO,email)).thenReturn(
                new ResponseEntity<>(
                        new StandardResponse(
                                200,
                                "Results entered",
                                "Result enter successfully !!!!"
                        ), HttpStatus.OK));

        ResponseEntity<StandardResponse> response = resultsController.addResults(accessToken , resultsRequestDTO);

        verify(generateJWT).validateToken(accessToken);
        verify(generateJWT).extractSubject(accessToken);
        verify(resultService).addResult(resultsRequestDTO ,email);
        assertResponseStatus(response , HttpStatus.OK , "Results entered","Result enter successfully !!!!");
    }

    @Test
    public void addResultsAccessDenied(){
        when(generateJWT.validateToken(accessToken)).thenReturn(new AuthenticationVerification(true , "user"));

        ResponseEntity<StandardResponse> response = resultsController.addResults(accessToken , resultsRequestDTO);

        verify(generateJWT).validateToken(accessToken);

        assertResponseStatus(response , HttpStatus.FORBIDDEN , "User hasn't access","Access denied !!!!");
    }

    @Test
    public void addResultUnauthorized(){
        when(generateJWT.validateToken(accessToken)).thenReturn(new AuthenticationVerification(false ));

        ResponseEntity<StandardResponse> response = resultsController.addResults(accessToken , resultsRequestDTO);

        verify(generateJWT).validateToken(accessToken);
        assertResponseStatus(response , HttpStatus.UNAUTHORIZED , "Unauthorized Access","Sign in failed !!!!");
    }

    private void assertResponseStatus(ResponseEntity<StandardResponse> response, HttpStatus expectedStatus , String msg , String data) {
        assert response.getStatusCode().equals(expectedStatus);
        assert response.getBody().getMessage().equals(msg);
        assert response.getBody().getData().equals(data);
    }



}
