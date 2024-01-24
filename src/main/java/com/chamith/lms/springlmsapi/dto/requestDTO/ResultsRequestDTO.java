package com.chamith.lms.springlmsapi.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultsRequestDTO {
    private String student_name ;
    private String subject;
    private String results;
    private String email;
}
