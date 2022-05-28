package com.ScalableTeam.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowUserBody {
    private String userID;
    private String requestedFollowUserID;
}
