package com.ScalableTeam.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
    private boolean successful;
    private String message;
    private String authToken;

    public LoginResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
        this.authToken = "";
    }
}
