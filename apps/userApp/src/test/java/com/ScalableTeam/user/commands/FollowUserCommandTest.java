package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.BlockedUserBody;
import com.ScalableTeam.models.user.BlockedUserResponse;
import com.ScalableTeam.models.user.FollowUserBody;
import com.ScalableTeam.models.user.FollowUserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest
class FollowUserCommandTest {

    @Autowired
    UserRepository userRepository;

    String userId;
    String followId;
    @Autowired
    FollowUserCommand followUserCommand;
    @Autowired
    private BlockUserCommand blockUserCommand;

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
        followId = user2.getUserNameId();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(userId);
        userRepository.deleteById(followId);
    }

    @Test
    void successfulFollow() {
        FollowUserBody followUserBody = new FollowUserBody(userId, followId);
        FollowUserResponse followUserResponse = followUserCommand.execute(followUserBody);
        assertEquals(followUserResponse.getMessage(), "User followed successfully");
    }

    @Test
    void followYourself() {
        FollowUserBody followUserBody = new FollowUserBody(userId, userId);
        FollowUserResponse followUserResponse = followUserCommand.execute(followUserBody);
        assertEquals(followUserResponse.getMessage(), "Can't follow yourself");
    }

    @Test
    void followedNotInDB() {
        FollowUserBody followUserBody = new FollowUserBody(userId, "wqee");
        FollowUserResponse followUserResponse = followUserCommand.execute(followUserBody);
        assertEquals(followUserResponse.getMessage(), "The followed user not found in DB!");
    }

    @Test
    void userNotInDB() {
        FollowUserBody followUserBody = new FollowUserBody("wqee", followId);
        FollowUserResponse followUserResponse = followUserCommand.execute(followUserBody);
        assertEquals(followUserResponse.getMessage(), "User not found in DB!");
    }


    @Test
    void followBlocked() {
        BlockedUserBody body = new BlockedUserBody(userId, followId);
        blockUserCommand.execute(body);
        FollowUserBody followUserBody = new FollowUserBody(userId, followId);
        FollowUserResponse followUserResponse = followUserCommand.execute(followUserBody);
        assertEquals(followUserResponse.getMessage(), "you have blocked this user!");
    }

    @Test
    void followBlockedYou() {
        BlockedUserBody body = new BlockedUserBody(followId,userId);
        blockUserCommand.execute(body);
        FollowUserBody followUserBody = new FollowUserBody(userId, followId);
        FollowUserResponse followUserResponse = followUserCommand.execute(followUserBody);
        assertEquals(followUserResponse.getMessage(), "This followed user has blocked you!");
    }
}