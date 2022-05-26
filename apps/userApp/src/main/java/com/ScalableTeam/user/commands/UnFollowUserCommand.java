package com.ScalableTeam.user.commands;

import com.ScalableTeam.user.entity.User;
import com.ScalableTeam.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import models.UnFollowUserBody;
import models.UnFollowUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

//@ComponentScan("com.ScalableTeam.user")
@Service
@AllArgsConstructor
public class UnFollowUserCommand implements ICommand<UnFollowUserBody, UnFollowUserResponse> {
    @Autowired
    private final UserRepository userRepository;

    public UnFollowUserResponse execute(UnFollowUserBody body){

        String  userId = body.getUserID();
        String unFollowUserId= body.getRequestedUnFollowUserID();

        Optional<User> unfollowUser = userRepository.findById(unFollowUserId);
        if(!unfollowUser.isPresent()){
            return new UnFollowUserResponse(false,"The unfollowed user not found in DB!");
        }

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            return new UnFollowUserResponse(false,"User not found in DB!");
        }


        HashMap<String, Boolean> follow = new HashMap<String, Boolean>();
        follow.put(unFollowUserId, false);
        userRepository.updateFollowedUsersWithID(userId, follow);

        return new UnFollowUserResponse(false,"User unfollowed successfully");

    }
}
