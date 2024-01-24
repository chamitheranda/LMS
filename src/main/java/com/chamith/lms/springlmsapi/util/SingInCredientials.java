package com.chamith.lms.springlmsapi.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SingInCredientials {
    private String token ;
    private HttpStatus httpStatus ;

    public SingInCredientials(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
