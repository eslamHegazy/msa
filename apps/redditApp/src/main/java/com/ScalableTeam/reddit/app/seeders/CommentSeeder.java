package com.ScalableTeam.reddit.app.seeders;

import com.ScalableTeam.arango.Comment;
import com.ScalableTeam.arango.CommentToComment;
import com.ScalableTeam.arango.PostToComment;
import com.ScalableTeam.reddit.app.repository.CommentChildrenHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostCommentHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@AllArgsConstructor
@Component
@Slf4j
public class CommentSeeder {
    private static final String prefix = "c";
    private static final String body = "World";
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostCommentHierarchyRepository postCommentHierarchyRepository;
    private final CommentChildrenHierarchyRepository commentChildrenHierarchyRepository;

    public Set<String> seedComments(Set<String> userNameIds) {
        log.info("Seed Comments:-----");
        ArrayList<String> users = new ArrayList<>(userNameIds);
        Set<String> comments = new HashSet<>();
        String user = users.get(rand(users));
        int commentNum = 1;

        String post = "p0";
        String id = prefix + commentNum++;
        comments.add(id);
        Comment comment1 = Comment.builder()
                .id(id)
                .userNameId(user)
                .postId(post)
                .commentOnPost(true)
                .commentParentId(post)
                .body(body)
                .upvoteCount(0)
                .downvoteCount(0)
                .build();
        commentRepository.save(comment1);

        id = prefix + commentNum++;
        comments.add(id);
        Comment comment2 = Comment.builder()
                .id(id)
                .userNameId(user)
                .postId(post)
                .commentOnPost(false)
                .commentParentId(comment1.getId())
                .body(body)
                .upvoteCount(0)
                .downvoteCount(0)
                .build();
        commentRepository.save(comment2);

        id = prefix + commentNum++;
        comments.add(id);
        Comment comment3 = Comment.builder()
                .id(id)
                .userNameId(user)
                .postId(post)
                .commentOnPost(false)
                .commentParentId(comment2.getId())
                .body(body)
                .upvoteCount(0)
                .downvoteCount(0)
                .build();
        commentRepository.save(comment3);

        id = prefix + commentNum++;
        comments.add(id);
        Comment comment4 = Comment.builder()
                .id(id)
                .userNameId(user)
                .postId(post)
                .commentOnPost(false)
                .commentParentId(comment3.getId())
                .body(body)
                .upvoteCount(0)
                .downvoteCount(0)
                .build();
        commentRepository.save(comment4);

        id = prefix + commentNum++;
        comments.add(id);
        Comment comment5 = Comment.builder()
                .id(id)
                .userNameId(user)
                .postId(post)
                .commentOnPost(false)
                .commentParentId(comment1.getId())
                .body(body)
                .upvoteCount(0)
                .downvoteCount(0)
                .build();
        commentRepository.save(comment5);

        id = prefix + commentNum++;
        comments.add(id);
        Comment comment6 = Comment.builder()
                .id(id)
                .userNameId(user)
                .postId(post)
                .commentOnPost(true)
                .commentParentId(post)
                .body(body)
                .upvoteCount(0)
                .downvoteCount(0)
                .build();
        commentRepository.save(comment6);

        PostToComment postToComment = PostToComment.builder()
                .post(postRepository.findById(post).get())
                .comment(commentRepository.findById(comment1.getId()).get())
                .build();
        postCommentHierarchyRepository.save(postToComment);
        postToComment = PostToComment.builder()
                .post(postRepository.findById(post).get())
                .comment(commentRepository.findById(comment6.getId()).get())
                .build();
        postCommentHierarchyRepository.save(postToComment);

        CommentToComment commentToComment = CommentToComment.builder()
                .parentComment(commentRepository.findById(comment1.getId()).get())
                .childComment(commentRepository.findById(comment2.getId()).get())
                .build();
        commentChildrenHierarchyRepository.save(commentToComment);
        commentToComment = CommentToComment.builder()
                .parentComment(commentRepository.findById(comment2.getId()).get())
                .childComment(commentRepository.findById(comment3.getId()).get())
                .build();
        commentChildrenHierarchyRepository.save(commentToComment);
        commentToComment = CommentToComment.builder()
                .parentComment(commentRepository.findById(comment3.getId()).get())
                .childComment(commentRepository.findById(comment4.getId()).get())
                .build();
        commentChildrenHierarchyRepository.save(commentToComment);
        commentToComment = CommentToComment.builder()
                .parentComment(commentRepository.findById(comment1.getId()).get())
                .childComment(commentRepository.findById(comment5.getId()).get())
                .build();
        commentChildrenHierarchyRepository.save(commentToComment);

        return comments;
    }

    static int rand(ArrayList<String> userNameIds) {
        Random rand = new Random();
        return rand.nextInt(userNameIds.size());
    }
}
