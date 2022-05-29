package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.ReportUserBody;
import com.ScalableTeam.models.user.ReportedUserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class ReportUserCommandTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportUserCommand reportUserCommand;

    private ArrayList<String> savedUsers;

    @BeforeEach
    void setup() {
        savedUsers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = User.builder().build();
            user.setReportedUsers(new HashMap<>());
            userRepository.save(user);
            savedUsers.add(user.getUserNameId());
        }
    }

    @Test
    void userReportHimself(){
        ReportUserBody body = new ReportUserBody(savedUsers.get(0),savedUsers.get(0),"dummy reason");
        ReportedUserResponse response = reportUserCommand.execute(body);
        assertFalse(response.isSuccessful());
        assertEquals(response.getMessage(), "The user can not report himself/herself!");
    }

    @Test
    void reportNonExistingUser(){
        ReportUserBody body = new ReportUserBody(savedUsers.get(0),"0000","dummy reason");
        ReportedUserResponse response = reportUserCommand.execute(body);
        assertFalse(response.isSuccessful());
        assertEquals(response.getMessage(), "The reported user not found in DB!");
    }

    @Test
    void userNotFound(){
        ReportUserBody body = new ReportUserBody("0000",savedUsers.get(0),"dummy reason");
        ReportedUserResponse response = reportUserCommand.execute(body);
        assertFalse(response.isSuccessful());
        assertEquals(response.getMessage(), "User not found in DB!");
    }

    @Test
    public void reportSuccessfully(){
        ReportUserBody body = new ReportUserBody(savedUsers.get(1),savedUsers.get(0),"dummy reason");
        ReportedUserResponse response = reportUserCommand.execute(body);
        assertTrue(response.isSuccessful());
        assertEquals(response.getMessage(), "User reported successfully");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
