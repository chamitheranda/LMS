package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.util.StandardResponse;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<StandardResponse> updatePrivilege(String email);

    ResponseEntity<StandardResponse> deleteUser(String email);

    ResponseEntity<StandardResponse> updateResults(String email, String result, String subject);
}
