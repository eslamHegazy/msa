package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.DeleteAccountBody;
import com.ScalableTeam.models.user.DeleteAccountResponse;
import com.ScalableTeam.user.entity.UserProfile;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;


@DataJpaTest
@ActiveProfiles("test")
class DeleteAccountCommandTest {

    String userId = "Mo99";
    String email = "mo99@gmail.com";
    String password = "pass";

    @MockBean
    UserRepository userRepository;
    @Autowired
    UserProfileRepository userProfileRepository;

    DeleteAccountCommand deleteAccountCommand;
    @BeforeEach
    void setUp() {
        userProfileRepository.save(new UserProfile(userId, email, password, null));
        deleteAccountCommand = new DeleteAccountCommand(userProfileRepository, userRepository);
    }

    @Test
    void deleteUsingWrongUsername(){
        String wrongId = "Mo20";
        doNothing().when(userRepository).deleteById(wrongId);
        DeleteAccountBody deleteAccountBody = new DeleteAccountBody(wrongId);
        DeleteAccountResponse deleteAccountResponse = deleteAccountCommand.execute(deleteAccountBody);
        assertFalse(deleteAccountResponse.isSuccessful());
        assertFalse(userProfileRepository.findAll().isEmpty());
    }

    @Test
    void successfulDelete(){
        doNothing().when(userRepository).deleteById(userId);
        DeleteAccountBody deleteAccountBody = new DeleteAccountBody(userId);
        DeleteAccountResponse deleteAccountResponse = deleteAccountCommand.execute(deleteAccountBody);
        assertTrue(deleteAccountResponse.isSuccessful());
        assertTrue(userProfileRepository.findAll().isEmpty());
    }
}