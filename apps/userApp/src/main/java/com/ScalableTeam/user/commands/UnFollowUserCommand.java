package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.UnFollowUserBody;
import com.ScalableTeam.models.user.UnFollowUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UnFollowUserCommand implements ICommand<UnFollowUserBody, UnFollowUserResponse> {
    @Autowired
    private final UserRepository userRepository;

    public UnFollowUserResponse execute(UnFollowUserBody body) {

        String userId = body.getUserID();
        String unFollowUserId = body.getRequestedUnFollowUserID();

        if (userId.equals(unFollowUserId)) {
            return new UnFollowUserResponse(false, "Can't unfollow yourself");
        }

        Optional<User> unfollowUser = userRepository.findById(unFollowUserId);
        if (!unfollowUser.isPresent()) {
            return new UnFollowUserResponse(false, "The unfollowed user not found in DB!");
        }

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return new UnFollowUserResponse(false, "User not found in DB!");
        }

        HashMap<String, Boolean> follow = user.get().getFollowedUsers();
        if (follow == null || !follow.containsKey(unFollowUserId)) {
            return new UnFollowUserResponse(false, "There is no followed user");
        }
        follow.remove(unFollowUserId);
        user.get().setFollowedUsers(follow);
        userRepository.save(user.get());

        return new UnFollowUserResponse(true, "User unfollowed successfully");

    }
}
