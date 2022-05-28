package com.ScalableTeam.reddit.app.requestForms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReportPostForm {
    private String userId;
    private String postId;
//    private String reason;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

//    public String getReason() {
//        return reason;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }

    @Override
    public String toString() {
        return "ReportPostForm{" +
                "userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                '}';
    }
}
