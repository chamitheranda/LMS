package com.chamith.lms.springlmsapi.testServices.resultServiceTest;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import com.chamith.lms.springlmsapi.dto.responseDTO.ViewResultsResponseDTO;
import com.chamith.lms.springlmsapi.mappers.ResultMapper;
import com.chamith.lms.springlmsapi.mappers.UserMapper;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestResultsView {

    @InjectMocks
    private ResultServiceImpl resultService ;

    @Mock
    private UserMapper userMapper ;

    @Mock
    private ResultMapper resultMapper ;

    private String accessToken ;
    private String email ;
    private List<ViewResultsResponseDTO> results ;

    @Before
    public void setUp(){
        accessToken = "token";
        email = "example@gmail.com";
        results = new ArrayList<>();

        ViewResultsResponseDTO result1 = new ViewResultsResponseDTO(
                "name",
                "results"
        );
        results.add(result1);
    }

    @Test
    public void testViewResultsNotFound(){
        when(userMapper.doesEmailExist(email)).thenReturn(false);

        ResponseEntity<StandardResponse> response = resultService.viewResults(email);

        verify(userMapper).doesEmailExist(email);

        assertResponseStatus(
                response,
                HttpStatus.NOT_FOUND,
                "No Results for = " + email ,
                "Results Not Found  !!!!"
        );
    }

    @Test
    public void testViewResultsSucess(){
        when(userMapper.doesEmailExist(email)).thenReturn(true);
        when(userMapper.getAllResults(email)).thenReturn(results);

        ResponseEntity<StandardResponse> response = resultService.viewResults(email);

        verify(userMapper).doesEmailExist(email);
        verify(userMapper).getAllResults(email);

        assertResponseStatus(
                response,
                HttpStatus.OK,
                "This is the results "  ,
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
