package com.ScalableTeam.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportedUserResponse {
    private boolean successful;
    private String message;
}
