package com.ScalableTeam.user.amqp.commands;

import com.password4j.Password;
import com.ScalableTeam.user.amqp.entity.UserProfile;
import com.ScalableTeam.user.amqp.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import models.LoginBody;
import models.LoginResponse;
import org.springframework.stereotype.Service;
import com.ScalableTeam.user.amqp.repositories.UserProfileRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginCommand implements ICommand<LoginBody, LoginResponse> {
    private final UserProfileRepository userProfileRepository;
    private final JwtUtil jwtUtil;
    @Override
    public LoginResponse execute(LoginBody body){
        String userId = body.getUserId();
        String password = body.getPassword();


        Optional<UserProfile> userProfile = userProfileRepository.findById(userId);
        if(userProfile.isEmpty())
            return new LoginResponse(false, "Wrong username");

        boolean isVerified = Password.check(password, userProfile.get().getPassword()).withBCrypt();

        if(!isVerified)
            return new LoginResponse(false, "Wrong username or password");

        String authToken = jwtUtil.generateToken(userId);

        //TODO: save the token to redis cache
        return new LoginResponse(true,  "Logged in Successfully", authToken);
    }
}
