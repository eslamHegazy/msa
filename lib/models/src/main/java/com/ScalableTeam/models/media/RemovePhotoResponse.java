package com.ScalableTeam.models.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemovePhotoResponse {
    private String message;
    private boolean successful;
}
