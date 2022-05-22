package com.ScalableTeam.reddit.app.requestForms;

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
