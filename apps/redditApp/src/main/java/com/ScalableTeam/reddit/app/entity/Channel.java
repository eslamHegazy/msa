package com.ScalableTeam.reddit.app.entity;
import com.ScalableTeam.reddit.app.requestForms.ReportPostForm;
import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;

@Document("channels")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Channel {
    @Id // db document field: _key
    private String channelNameId;

    @ArangoId // db document field: _id
    private String arangoId;

    private String adminId;
    private HashMap<String,Boolean> moderators;
    private HashMap<String, Boolean> bannedUsers;
//    private HashMap<String, ReportPostForm> reports;
    private HashMap<String, Boolean>reports;

    public String getChannelNameId() {
        return channelNameId;
    }

    public void setChannelNameId(String channelNameId) {
        this.channelNameId = channelNameId;
    }

    public String getArangoId() {
        return arangoId;
    }

    public void setArangoId(String arangoId) {
        this.arangoId = arangoId;
    }



    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public HashMap<String,Boolean> getModerators() {return moderators;}

    public void setModerators(HashMap<String,Boolean> moderators) {
        this.moderators = moderators;
    }

    public HashMap<String, Boolean> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(HashMap<String, Boolean> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }

    public HashMap<String, Boolean> getReports() {
        return reports;
    }

    public void setReports(HashMap<String,Boolean> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "Channel [id=" + channelNameId +", adminId=" + adminId + moderators.toString()+"]";
    }



    public Channel(String channelNameId, String adminId) {
        this.channelNameId = channelNameId;
        this.adminId = adminId;
        moderators.put(adminId,true);
    }
}
