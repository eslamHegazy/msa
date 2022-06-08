package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.*;
import com.ScalableTeam.user.entity.UserProfile;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class EditProfileCommandTest {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    UserRepository userRepository;

    String userId = "Mo99";

    EditProfileCommand editProfileCommand;

    @BeforeEach
    void setUp() {
        List<UserProfile> profiles = List.of(
                new UserProfile("Mo98", "mo@gmail.com", "pass", ""),
                new UserProfile("es2000", "es@gmail.com", "pass", "")
        );

        List<User> users = profiles.stream()
                .map(profile -> User.builder().userNameId(profile.getUserId()).email(profile.getEmail()).build())
                .collect(Collectors.toList());

        userProfileRepository.saveAll(profiles);
        userRepository.saveAll(users);
        editProfileCommand = new EditProfileCommand(userProfileRepository);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(userId);
    }

    @Test
    void successfulEdit() {
        EditProfileBody editProfileBody = new EditProfileBody(userId, "hala@mala.bal");
        EditProfileResponse editProfileResponse = editProfileCommand.execute(editProfileBody);
        assertTrue(editProfileResponse.isSuccessful());
        assertEquals(editProfileResponse.getMessage(), "The profile was updated successfully");

    }
    @Test
    void editToInvalidEmailFormat(){
        EditProfileBody editProfileBody = new EditProfileBody(userId, "mogmail.com");
        EditProfileResponse editProfileResponse = editProfileCommand.execute(editProfileBody);
        assertFalse(editProfileResponse.isSuccessful());
        assertEquals(editProfileResponse.getMessage(), "Not a valid email format");
    }

    @Test
    void editToExistingEmail() {
        EditProfileBody editProfileBody = new EditProfileBody(userId, "es@gmail.com");
        EditProfileResponse editProfileResponse = editProfileCommand.execute(editProfileBody);
        assertFalse(editProfileResponse.isSuccessful());
        assertEquals(editProfileResponse.getMessage(), "Email already exists");
    }
}
