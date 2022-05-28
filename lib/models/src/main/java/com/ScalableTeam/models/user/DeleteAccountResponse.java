package com.ScalableTeam.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteAccountResponse {
    private boolean successful;
    private String message;
}
