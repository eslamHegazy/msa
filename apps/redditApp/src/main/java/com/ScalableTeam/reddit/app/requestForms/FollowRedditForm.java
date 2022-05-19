package com.ScalableTeam.reddit.app.requestForms;

import java.util.HashMap;

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
