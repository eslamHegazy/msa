package mocks;

import com.ScalableTeam.arango.Post;
import com.sun.istack.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostMock {
    public final static String upvotePostFirstTime = "upvoted your post with id";
    public final static String upvotePostSecondTime = "removed their upvote on your post with id";
    public final static String upvoteAfterDownvote = "removed their downvote and upvoted instead your post with id";
    public final static String downvotePostFirstTime = "downvoted your post with id";
    public final static String downvotePostSecondTime = "removed their downvote on your post with id";
    public final static String downvoteAfterUpvote = "removed their upvote and downvoted instead your post with id";
    private static final String id = "MockPost";
    private static final String title = "Hello";
    private static final String body = "World";
    private static final String photoLink = "https://google.com/image/123";
    private static final String channelId = "MockChannel";
    private static final int upvoteCount = 0;
    private static final int downvoteCount = 0;

    public static Post getPost() {
        return Post.builder()
                .id(id)
                .userNameId(UserMock.getId())
                .title(title)
                .body(body)
                .photoLink(photoLink)
                .upvoteCount(upvoteCount)
                .downvoteCount(downvoteCount)
                .time(Date.from(Instant.now()))
                .channelId(channelId)
                .build();
    }

    public static String getChannelId() {
        return channelId;
    }

    public static String getId() {
        return id;
    }

    public static Post getPostWithTitle(String title) {
        Post post = getPost();
        post.setTitle(title);
        post.setId(String.valueOf(Objects.hash(title)));
        return post;
    }

    public static Post getPostWithUserNameId(String userNameId) {
        Post post = getPost();
        post.setUserNameId(id);
        return post;
    }

    public static Post getPostWithIdChannelId(String postId, String channelId) {
        Post post = getPost();
        post.setId(postId);
        post.setChannelId(channelId);
        return post;
    }

    public static List<Post> getPostsWithTitle(@NotNull String... titles) {
        return Arrays.stream(titles).map(PostMock::getPostWithTitle).
                collect(Collectors.toList());
    }
}
