package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.BlockedUserBody;
import com.ScalableTeam.models.user.BlockedUserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BlockUserCommandTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockUserCommand blockUserCommand;
    private ArrayList<String> savedUsers;
    @BeforeEach
    void setup(){
        savedUsers=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user=User.builder().build();
            user.setBlockedUsers(new HashMap<>());
            userRepository.save(user);
            savedUsers.add(user.getUserNameId());
        }
    }

    @Test
    void blockHimself() {
        BlockedUserBody body = new BlockedUserBody(savedUsers.get(0),savedUsers.get(0));
        BlockedUserResponse response = blockUserCommand.execute(body);
        assertFalse(response.isSuccessful());
        assertEquals(response.getMessage(), "The user can not block himself/herself!");
    }

    @Test
    void blockNonExistingUser() {
        BlockedUserBody body = new BlockedUserBody(savedUsers.get(0),"0000");
        BlockedUserResponse response = blockUserCommand.execute(body);
        assertFalse(response.isSuccessful());
        assertEquals(response.getMessage(), "The blocked user not found in DB!");
    }

    @Test
    void userNotFound() {
        BlockedUserBody body = new BlockedUserBody("0000",savedUsers.get(0));
        BlockedUserResponse response = blockUserCommand.execute(body);
        assertFalse(response.isSuccessful());
        assertEquals(response.getMessage(), "User not found in DB!");
    }

    @Test
    void blockSuccessfully() {
        BlockedUserBody body = new BlockedUserBody(savedUsers.get(1),savedUsers.get(0));
        BlockedUserResponse response = blockUserCommand.execute(body);
        assertTrue(response.isSuccessful());
        assertEquals(response.getMessage(), "User blocked successfully");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
