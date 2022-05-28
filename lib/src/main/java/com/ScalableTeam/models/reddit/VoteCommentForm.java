package com.ScalableTeam.models.reddit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteCommentForm {
    private String commentId;
    private String userNameId;
}
