package com.ScalableTeam.reddit.app.requestForms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashMap;
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FollowRedditForm {


    private String  userId ;
    private String redditId ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRedditId() {
        return redditId;
    }

    public void setRedditId(String redditId) {
        this.redditId = redditId;
    }
}
