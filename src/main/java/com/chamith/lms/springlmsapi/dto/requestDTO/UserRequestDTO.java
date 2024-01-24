package com.chamith.lms.springlmsapi.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDTO {
    private String name ;
    private String email ;
    private String contactNumber ;
    private String password ;
}

