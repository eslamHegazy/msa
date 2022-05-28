package com.ScalableTeam.models.reddit;

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
