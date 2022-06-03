package com.ScalableTeam.arango;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import com.arangodb.springframework.core.convert.resolver.LazyLoadingProxy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

@Document("channels")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Channel implements Serializable {
    @Id // db document field: _key
    private String channelNameId;

    @ArangoId // db document field: _id
    private String arangoId;

    @Relations(edges = RedditFollowersEdge.class, lazy = true, direction = Relations.Direction.OUTBOUND)
    private Collection<User> followers;
    private String adminId;
    private HashMap<String, Boolean> moderators;
    private HashMap<String, Boolean> bannedUsers;
    //    private HashMap<String, ReportPostForm> reports;
    private HashMap<String, Boolean> reports;

    public Channel(String channelNameId, String adminId) {
        this.channelNameId = channelNameId;
        this.adminId = adminId;
        moderators.put(adminId, true);
    }

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

    public HashMap<String, Boolean> getModerators() {
        return moderators;
    }

    public void setModerators(HashMap<String, Boolean> moderators) {
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

//    @Override
//    public String toString() {
//        return "Channel [id=" + channelNameId +", adminId=" + adminId + "]";
//    }

    public void setReports(HashMap<String, Boolean> reports) {
        this.reports = reports;
    }

    public LazyLoadingProxy getFollowers() {
        return (LazyLoadingProxy) followers;
    }

    public void setFollowers(Collection<User> followers) {
        this.followers = followers;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelNameId='" + channelNameId + '\'' +
                ", arangoId='" + arangoId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", moderators=" + moderators +
                ", bannedUsers=" + bannedUsers +
                ", reports=" + reports +
                '}';
    }
}
