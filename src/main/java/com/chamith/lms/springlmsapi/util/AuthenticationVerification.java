package com.chamith.lms.springlmsapi.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class AuthenticationVerification {
    private boolean authenticationStatus ;
    private String privilegeLevel ;

    public AuthenticationVerification(boolean authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }
}
