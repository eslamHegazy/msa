package com.ScalableTeam.reddit.app.seeders;

import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
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

    public Set<String> seedComments(Set<String> userNameIds, Set<String> postIds) {
        log.info("Seed Comments:-----");
        ArrayList<String> users = new ArrayList<>(userNameIds);
        ArrayList<String> posts = new ArrayList<>(postIds);
        Set<String> comments = new HashSet<>();
        int commentNum = 0;
        for (int i = 0; i < posts.size(); i++) {
            String post = posts.get(i);
            for (int j = 0; j < i + 1; j++) {
                String user = users.get(rand(users));
                String id = prefix + commentNum++;
                comments.add(id);
                Comment comment = Comment.builder()
                        .id(id)
                        .userNameId(user)
                        .postId(post)
                        .body(body)
                        .commentOnPost(true)
                        .commentParentId(post)
                        .upvoteCount(0)
                        .downvoteCount(0)
                        .build();
                commentRepository.save(comment);
            }
        }
        return comments;
    }

    static int rand(ArrayList<String> userNameIds) {
        Random rand = new Random();
        return rand.nextInt(userNameIds.size());
    }
}
