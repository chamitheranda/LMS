package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.EnrollRequestDTO;
import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<StandardResponse> enroll(EnrollRequestDTO enrollRequestDTO, String email);
}
