package com.chamith.lms.springlmsapi.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignInRequestDTO {
    private String email ;
    private String password ;
}
