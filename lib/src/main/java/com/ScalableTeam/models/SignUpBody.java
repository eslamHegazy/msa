package com.ScalableTeam.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpBody{
    private String userId;
    private String email;
    private String password;
}
