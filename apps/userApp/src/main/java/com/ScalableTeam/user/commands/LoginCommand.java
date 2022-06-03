package com.ScalableTeam.user.commands;

import com.ScalableTeam.models.user.LoginBody;
import com.ScalableTeam.models.user.LoginResponse;
import com.ScalableTeam.user.caching.RedisUtility;
import com.ScalableTeam.user.entity.UserProfile;
import com.ScalableTeam.user.jwt.JwtUtil;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import com.password4j.Password;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginCommand implements ICommand<LoginBody, LoginResponse> {
    private final UserProfileRepository userProfileRepository;
    private final JwtUtil jwtUtil;

    private final RedisUtility redisUtility;

    @Override
    public LoginResponse execute(LoginBody body) {
        String userId = body.getUserId();
        String password = body.getPassword();


        Optional<UserProfile> userProfile = userProfileRepository.findById(userId);
        if (userProfile.isEmpty())
            return new LoginResponse(false, "Wrong username");

        boolean isVerified = Password.check(password, userProfile.get().getPassword()).withBCrypt();

        if (!isVerified)
            return new LoginResponse(false, "Wrong username or password");

        String authToken = jwtUtil.generateToken(userId);

        redisUtility.setValue(userId, authToken);
        return new LoginResponse(true, "Logged in Successfully", authToken);
    }
}
