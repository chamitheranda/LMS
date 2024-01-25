package com.chamith.lms.springlmsapi.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ViewResultsResponseDTO {
    private String subject;
    private String results;
}
