package mocks;

import com.ScalableTeam.arango.Channel;

import java.util.HashMap;

public class ChannelMock2 {
    private static String channelNameId = "newChannel";
    private static String adminId = UserMock.getId();
    public static Channel getChannelWithAdminAsModerator(){
        HashMap<String,Boolean>moderators=new HashMap<>();
        moderators.put(adminId,true);
        return Channel.builder().channelNameId(channelNameId).adminId(adminId).moderators(moderators).build();
    }

    public static String getChannelNameId() {
        return channelNameId;
    }

    public static String getAdminId() {
        return adminId;
    }
}
