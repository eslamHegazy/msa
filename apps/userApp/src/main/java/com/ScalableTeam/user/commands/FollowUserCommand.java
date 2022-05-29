package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.FollowUserBody;
import com.ScalableTeam.models.user.FollowUserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

//@ComponentScan("com.ScalableTeam.user")
@Service
@AllArgsConstructor
@Slf4j
public class FollowUserCommand implements ICommand<FollowUserBody, FollowUserResponse> {
    @Autowired
    private final UserRepository userRepository;


    public FollowUserResponse execute(FollowUserBody body) {

        String userId = body.getUserID();
        String requestedFollowUserID = body.getRequestedFollowUserID();

        if(userId.equals(requestedFollowUserID)){
            return new FollowUserResponse(false,"Can't follow yourself");
        }
        Optional<User> followUser = userRepository.findById(requestedFollowUserID);
        if (!followUser.isPresent()) {
            return new FollowUserResponse(false, "The followed user not found in DB!");
        }

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return new FollowUserResponse(false, "User not found in DB!");
        }

        HashMap<String, Boolean> blocked = userRepository.findById(userId).get().getBlockedUsers();
        if (blocked != null && blocked.containsKey(requestedFollowUserID)) {
            return new FollowUserResponse(false, "you have blocked this user!");
        }

        HashMap<String, Boolean> MeBlocked = userRepository.findById(requestedFollowUserID).get().getBlockedUsers();
        if (MeBlocked != null && MeBlocked.containsKey(userId)) {
            return new FollowUserResponse(false, "This followed user has blocked you!");
        }

        HashMap<String, Boolean> follow = new HashMap<String, Boolean>();
        follow.put(requestedFollowUserID, true);
//        userRepository.updateFollowedUsersWithID(userId, follow);
        return new FollowUserResponse(true, "User followed successfully");

    }
}
