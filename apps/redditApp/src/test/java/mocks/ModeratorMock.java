package mocks;


import com.ScalableTeam.arango.User;

import java.util.HashMap;

public class ModeratorMock {
    private static final String id = "ModeratorMock";

    public static User getModerator() {
        return User.builder()
                .userNameId(id)
                .build();
    }

    public static String getId() {
        return id;
    }

    public static User getModeratorWithId(String id) {
        User user = getModerator();
        user.setUserNameId(id);
        return user;
    }

    public static User getModeratorFollowsChannel(String channelId) {
        User user = getModerator();
        HashMap<String, Boolean> follows = new HashMap<>();
        follows.put(channelId, true);
        user.setFollowedChannels(follows);
        return user;
    }

}
