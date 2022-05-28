package com.ScalableTeam.user.commands;

import com.ScalableTeam.models.user.LoginBody;
import com.ScalableTeam.models.user.LoginResponse;
import com.ScalableTeam.user.entity.UserProfile;
import com.ScalableTeam.user.jwt.JwtUtil;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import com.password4j.Hash;
import com.password4j.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LoginCommandTest {

    @Autowired
    UserProfileRepository userProfileRepository;

    String userId = "Mo99";
    String email = "mo99@gmail.com";
    String password = "pass";

    static JwtUtil jwtUtil;

    LoginCommand loginCommand;
    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        Hash hash = Password.hash(password).withBCrypt();
        UserProfile userProfile = new UserProfile(userId, email, hash.getResult(), null);
        userProfileRepository.save(userProfile);
        loginCommand = new LoginCommand(userProfileRepository, jwtUtil);
    }

    @Test
    void successfulLogin(){
        LoginBody loginBody = new LoginBody(userId, password);
        LoginResponse loginResponse = loginCommand.execute(loginBody);
        assertTrue(loginResponse.isSuccessful());
    }

    @Test
    void loginWithWrongUsername(){
        LoginBody loginBody = new LoginBody("Mo98", password);
        LoginResponse loginResponse = loginCommand.execute(loginBody);
        assertFalse(loginResponse.isSuccessful());
        assertEquals(loginResponse.getMessage(), "Wrong username");
    }

    @Test
    void loginWithWrongPassword(){
        LoginBody loginBody = new LoginBody(userId, "wrong pass");
        LoginResponse loginResponse = loginCommand.execute(loginBody);
        assertFalse(loginResponse.isSuccessful());
        assertEquals(loginResponse.getMessage(), "Wrong username or password");
    }
}