package com.ScalableTeam.reddit.app.requestForms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class VotePostForm {
    private String postId;
    private String userNameId;
}
