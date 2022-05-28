package com.ScalableTeam.user.commands;

import com.ScalableTeam.models.user.SignUpBody;
import com.ScalableTeam.models.user.SignUpResponse;
import com.ScalableTeam.user.entity.UserProfile;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SignUpCommandTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    String userId = "Mo99";
    String email = "mo99@gmail.com";
    String password = "pass";

    private SignUpCommand signUpCommand;

    @BeforeEach
    void setUp() {
        List<UserProfile> users = List.of(
            new UserProfile("Mo98","mo@gmail.com", "pass", ""),
            new UserProfile("es2000","es@gmail.com", "pass", ""),
            new UserProfile("Hus2000","hus@gmail.com", "pass", ""),
            new UserProfile("Abdo99","abdo@gmail.com", "pass", "")
        );
        userProfileRepository.saveAll(users);
        signUpCommand = new SignUpCommand(userProfileRepository);
    }

    @Test
    void signUpWithExistingUsername() {
        SignUpBody signUpBody = new SignUpBody("Mo98", email, password);
        SignUpResponse signUpResponse = signUpCommand.execute(signUpBody);
        assertFalse(signUpResponse.isSuccessful());
        assertEquals(signUpResponse.getMessage(), "Username already exists");
    }

    @Test
    void signUpWithExistingEmail(){
        SignUpBody signUpBody = new SignUpBody(userId, "mo@gmail.com", password);
        SignUpResponse signUpResponse = signUpCommand.execute(signUpBody);
        assertFalse(signUpResponse.isSuccessful());
        assertEquals(signUpResponse.getMessage(), "Email already exists");
    }

    @Test
    void signUpWithInvalidEmailFormat(){
        SignUpBody signUpBody = new SignUpBody(userId, "mogmail.com", password);
        SignUpResponse signUpResponse = signUpCommand.execute(signUpBody);
        assertFalse(signUpResponse.isSuccessful());
        assertEquals(signUpResponse.getMessage(), "Not a valid email format");
    }

    @Test
    void signUpSuccessfully(){
        SignUpBody signUpBody = new SignUpBody(userId, email, password);
        SignUpResponse signUpResponse = signUpCommand.execute(signUpBody);
        assertTrue(signUpResponse.isSuccessful());
        assertEquals(signUpResponse.getMessage(), "Registration done successfully");
        assertTrue(userProfileRepository.existsById(userId));
    }
}