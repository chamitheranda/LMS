package com.chamith.lms.springlmsapi.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EnrollRequestDTO {
    private String subject;
    private String email;
}
