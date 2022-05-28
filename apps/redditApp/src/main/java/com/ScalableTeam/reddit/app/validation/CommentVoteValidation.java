package com.ScalableTeam.reddit.app.validation;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CommentVoteValidation {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public void validateCommentVote(String userNameId, String commentId) throws Exception {
        log.info("Validation for Comment Vote");
        boolean userExists = userRepository.existsById(userNameId);
        boolean commentExists = commentRepository.existsById(commentId);
        StringBuilder errorMessageBuilder = new StringBuilder();
        boolean requestRejected = false;
        if (!userExists) {
            errorMessageBuilder.append(String.format("User with id %s does not exist", userNameId));
            requestRejected = true;
        }
        if (!commentExists) {
            if (errorMessageBuilder.length() > 0) errorMessageBuilder.append("::");
            errorMessageBuilder.append(String.format("Comment with id %s does not exist", commentId));
            requestRejected = true;
        }
        if (requestRejected)
            throw new IllegalStateException(errorMessageBuilder.toString());
    }
}
