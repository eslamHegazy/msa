package mocks;

import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.Post;
import com.sun.istack.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.ScalableTeam.arango.Post;
import com.sun.istack.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class ChannelMock {


    private static final String id = "MockChannel";
    private static final String adminId = "Hello";

//    public final static String upvotePostFirstTime = "upvoted your post with id";
//    public final static String upvotePostSecondTime = "removed their upvote on your post with id";
//    public final static String upvoteAfterDownvote = "removed their downvote and upvoted instead your post with id";
//    public final static String downvotePostFirstTime = "downvoted your post with id";
//    public final static String downvotePostSecondTime = "removed their downvote on your post with id";
//    public final static String downvoteAfterUpvote = "removed their upvote and downvoted instead your post with id";

    public static Channel getChannel() {
        return Channel.builder()
                .channelNameId(id)
                .adminId(UserMock.getId())
                .build();
    }

    public static String getId() {
        return id;
    }

    public static String getAdminId(){return adminId;}

    public static Channel getChannelWithId(String channelId){
        Channel ch = getChannel();
        ch.setChannelNameId(channelId);
        return ch;
    }
    public static Channel getChannelWithAdminId(String adminId){
        Channel ch = getChannel();
        ch.setAdminId(adminId);
        return ch;
    }
    public static Channel getChannelWithAdminChannelId(String channelId,String adminId){
        Channel ch = getChannel();
        ch.setChannelNameId(channelId);
        ch.setAdminId(adminId);
        return ch;
    }


}

