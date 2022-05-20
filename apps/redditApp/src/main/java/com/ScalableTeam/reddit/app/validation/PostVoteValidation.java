package com.ScalableTeam.reddit.app.validation;

import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PostVoteValidation {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void validatePostVote(String userNameId, String postId) throws Exception {
        log.info("Validation for Post Vote");
        boolean userExists = userRepository.existsById(userNameId);
        boolean postExists = postRepository.existsById(postId);
        StringBuilder errorMessageBuilder = new StringBuilder();
        boolean requestRejected = false;
        if (!userExists) {
            errorMessageBuilder.append(String.format("User with id %s does not exist", userNameId));
            requestRejected = true;
        }
        if (!postExists) {
            if (errorMessageBuilder.length() > 0) errorMessageBuilder.append("::");
            errorMessageBuilder.append(String.format("Post with id %s does not exist", postId));
            requestRejected = true;
        }
        if (requestRejected)
            throw new IllegalStateException(errorMessageBuilder.toString());
    }
}
