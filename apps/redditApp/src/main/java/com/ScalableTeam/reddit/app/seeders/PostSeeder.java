package com.ScalableTeam.reddit.app.seeders;

import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Component
@Slf4j
public class PostSeeder {
    private static final String prefix = "p";
    private static final String title = "Hello";
    private static final String body = "World";
    private static final String photoLink = "https://google.com/image/123";
    private final PostRepository postRepository;

    public Set<String> seedPosts(Set<String> userNameIds) {
        log.info("Seed Posts:-----");
        ArrayList<String> users = new ArrayList<>(userNameIds);
        Set<String> posts = new HashSet<>();
        int postNum = 0;
        for (int i = 0; i < userNameIds.size(); i++) {
            String user = users.get(i);
            for (int j = 0; j < i + 1; j++) {
                String id = prefix + postNum++;
                posts.add(id);
                Post post = Post.builder()
                        .id(id)
                        .userNameId(user)
                        .title(title)
                        .body(body)
                        .photoLink(photoLink)
                        .upvoteCount(0)
                        .downvoteCount(0)
                        .time(Instant.now())
                        .build();
                postRepository.save(post);
            }
        }
        return posts;
    }
}
