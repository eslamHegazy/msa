package com.ScalableTeam.user.commands;

import com.ScalableTeam.user.entity.User;
import com.ScalableTeam.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.FollowUserBody;
import models.FollowUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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


    public FollowUserResponse execute(FollowUserBody body){

        String  userId = body.getUserID();
        String requestedFollowUserID= body.getRequestedFollowUserID();

        Optional<User> followUser = userRepository.findById(requestedFollowUserID);
        if(!followUser.isPresent()){
            return new FollowUserResponse(false,"The followed user not found in DB!");
        }

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            return new FollowUserResponse(false,"User not found in DB!");
        }

        HashMap<String, Boolean> blocked=userRepository.findById(userId).get().getBlockedUsers();
        if(blocked.containsKey(requestedFollowUserID)){
            return new FollowUserResponse(false,"This followed userUser not found in DB!");
        }


        HashMap<String, Boolean> follow = new HashMap<String, Boolean>();
        follow.put(requestedFollowUserID, true);
        userRepository.updateFollowedUsersWithID(userId, follow);
        return new FollowUserResponse(true,"User followed successfully");

    }
}
