package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UnfollowUserCommandTest {

    @Autowired
    UserRepository userRepository;

    String userId;
    String unfollowId;
    @Autowired
    UnFollowUserCommand unfollowUserCommand;
    @Autowired
    FollowUserCommand followUserCommand;


    @BeforeEach
    void setUp() {

        User user1 = User.builder().build();
        user1.setBlockedUsers(new HashMap<>());
        user1.setFollowedUsers(new HashMap<>());
        userRepository.save(user1);
        userId = user1.getUserNameId();
        User user2 = User.builder().build();
        user2.setBlockedUsers(new HashMap<>());
        user2.setFollowedUsers(new HashMap<>());
        userRepository.save(user2);
        unfollowId = user2.getUserNameId();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(userId);
        userRepository.deleteById(unfollowId);
    }

    @Test
    void successfulUnfollow() {
        FollowUserBody followUserBody = new FollowUserBody(userId, unfollowId);
        followUserCommand.execute(followUserBody);
        UnFollowUserBody unfollowUserBody = new UnFollowUserBody(userId, unfollowId);
        UnFollowUserResponse UnfollowUserResponse = unfollowUserCommand.execute(unfollowUserBody);
        assertEquals(UnfollowUserResponse.getMessage(), "User unfollowed successfully");
    }

    @Test
    void unfollowedNotInDB() {
        UnFollowUserBody unfollowUserBody = new UnFollowUserBody(userId, "unfollowId");
        UnFollowUserResponse UnfollowUserResponse = unfollowUserCommand.execute(unfollowUserBody);
        assertEquals(UnfollowUserResponse.getMessage(), "The unfollowed user not found in DB!");
    }

    @Test
    void userNotInDB() {
        UnFollowUserBody unfollowUserBody = new UnFollowUserBody("userId", unfollowId);
        UnFollowUserResponse UnfollowUserResponse = unfollowUserCommand.execute(unfollowUserBody);
        assertEquals(UnfollowUserResponse.getMessage(), "User not found in DB!");
    }

    @Test
    void unfollowYourself() {
        UnFollowUserBody unfollowUserBody = new UnFollowUserBody(userId, userId);
        UnFollowUserResponse UnfollowUserResponse = unfollowUserCommand.execute(unfollowUserBody);
        assertEquals(UnfollowUserResponse.getMessage(), "Can't unfollow yourself");
    }
}