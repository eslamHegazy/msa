package mocks;


import com.ScalableTeam.arango.User;

import java.util.HashMap;

public class UserMock {
    private static final String id = "MockUser";
    private static final String photoLink = "https://google.com/image/123";
    private static final String email = "someemail@gmail.com";
    private static final String password = "password";

    public static User getUser() {
        return User.builder()
                .userNameId(id)
                .email(email)
                .profilePhotoLink(photoLink)
                .password(password)
                .build();
    }

    public static String getId() {
        return id;
    }

    public static User getUserWithId(String id) {
        User user = getUser();
        user.setUserNameId(id);
        return user;
    }
    public static User getUserFollowsChannel(String channelId){
        User user = getUser();
        HashMap<String,Boolean>follows=new HashMap<>();
        follows.put(channelId,true);
        user.setFollowedChannels(follows);
        return user;
    }
}
