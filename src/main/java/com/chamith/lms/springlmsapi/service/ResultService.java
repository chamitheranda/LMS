package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.ResultsRequestDTO;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.http.ResponseEntity;

public interface ResultService {
    ResponseEntity<StandardResponse> addResult(ResultsRequestDTO resultsRequestDTO, String email);

    ResponseEntity<StandardResponse> viewResults(String email);
}
