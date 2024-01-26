package com.chamith.lms.springlmsapi.service;

import com.chamith.lms.springlmsapi.dto.requestDTO.SignInRequestDTO;
import com.chamith.lms.springlmsapi.dto.requestDTO.UserRequestDTO;
import com.chamith.lms.springlmsapi.util.SingInCredientials;

public interface AuthService {
    String addUser(UserRequestDTO userReqestDTO);

    SingInCredientials signIn(SignInRequestDTO signInRequestDTO);
}
