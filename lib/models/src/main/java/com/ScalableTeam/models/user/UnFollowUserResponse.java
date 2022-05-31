package com.ScalableTeam.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnFollowUserResponse {
    private boolean successful;
    private String message;
}
