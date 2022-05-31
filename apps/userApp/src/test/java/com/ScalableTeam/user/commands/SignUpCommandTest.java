package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.SignUpBody;
import com.ScalableTeam.models.user.SignUpResponse;
import com.ScalableTeam.user.entity.UserProfile;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignUpCommandTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    String userId = "Mo99";
    String email = "mo99@gmail.com";
    String password = "pass";

    private SignUpCommand signUpCommand;

    @BeforeEach
    void setUp() {
        List<UserProfile> profiles = List.of(
            new UserProfile("Mo98","mo@gmail.com", "pass", ""),
            new UserProfile("es2000","es@gmail.com", "pass", ""),
            new UserProfile("Hus2000","hus@gmail.com", "pass", ""),
            new UserProfile("Abdo99","abdo@gmail.com", "pass", "")
        );

        List<User> users = profiles.stream()
                .map(profile -> User.builder().userNameId(profile.getUserId()).email(profile.getEmail()).build())
                .collect(Collectors.toList());

        userProfileRepository.saveAll(profiles);
        userRepository.saveAll(users);

        signUpCommand = new SignUpCommand(userProfileRepository, userRepository);
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

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        userProfileRepository.deleteAll();
    }
}