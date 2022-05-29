package mocks;

import com.ScalableTeam.arango.Comment;

import java.time.Instant;
import java.util.Date;

public class CommentMock {
    private static final String id = "MockComment";
    private static final String body = "World";
    private static final int upvoteCount = 0;
    private static final int downvoteCount = 0;
    public final static String upvoteCommentFirstTime = "upvoted your comment with id";
    public final static String upvoteCommentSecondTime = "removed their upvote on your comment with id";
    public final static String upvoteAfterDownvote = "removed their downvote and upvoted instead your comment with id";
    public final static String downvoteCommentFirstTime = "downvoted your comment with id";
    public final static String downvoteCommentSecondTime = "removed their downvote on your comment with id";
    public final static String downvoteAfterUpvote = "removed their upvote and downvoted instead your comment with id";

    public static Comment getComment() {
        return Comment.builder()
                .id(id)
                .userNameId(UserMock.getId())
                .body(body)
                .commentParentId(PostMock.getId())
                .postId(PostMock.getId())
                .commentOnPost(true)
                .upvoteCount(upvoteCount)
                .downvoteCount(downvoteCount)
                .build();
    }

    public static String getId() {
        return id;
    }

    public static Comment getCommentWithUserNameId(String userNameId) {
        Comment comment = getComment();
        comment.setUserNameId(id);
        return comment;
    }
}
