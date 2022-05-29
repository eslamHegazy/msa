package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.DeleteAccountBody;
import com.ScalableTeam.models.user.DeleteAccountResponse;
import com.ScalableTeam.user.entity.UserProfile;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;


@SpringBootTest
@ActiveProfiles("test")
class DeleteAccountCommandTest {

    String userId = "Mo99";
    String email = "mo99@gmail.com";
    String password = "pass";

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserProfileRepository userProfileRepository;

    DeleteAccountCommand deleteAccountCommand;
    @BeforeEach
    void setUp() {
        userProfileRepository.save(new UserProfile(userId, email, password, null));
        userRepository.save(User.builder().userNameId(userId).email(email).build());
        deleteAccountCommand = new DeleteAccountCommand(userProfileRepository, userRepository);
    }

    @Test
    void deleteUsingWrongUsername(){
        String wrongId = "Mo20";
        DeleteAccountBody deleteAccountBody = new DeleteAccountBody(wrongId);
        DeleteAccountResponse deleteAccountResponse = deleteAccountCommand.execute(deleteAccountBody);
        assertFalse(deleteAccountResponse.isSuccessful());
        assertFalse(userProfileRepository.findAll().isEmpty());
        assertTrue(userRepository.existsById(userId));
    }

    @Test
    void successfulDelete(){
        DeleteAccountBody deleteAccountBody = new DeleteAccountBody(userId);
        DeleteAccountResponse deleteAccountResponse = deleteAccountCommand.execute(deleteAccountBody);
        assertTrue(deleteAccountResponse.isSuccessful());
        assertTrue(userProfileRepository.findAll().isEmpty());
        assertFalse(userRepository.existsByUserNameId(userId));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        userProfileRepository.deleteAll();
    }
}