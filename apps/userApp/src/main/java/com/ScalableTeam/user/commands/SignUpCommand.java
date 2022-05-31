package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.SignUpBody;
import com.ScalableTeam.models.user.SignUpResponse;
import com.ScalableTeam.user.entity.UserProfile;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import com.password4j.Hash;
import com.password4j.Password;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class SignUpCommand implements ICommand<SignUpBody, SignUpResponse> {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    @Override
    public SignUpResponse execute(SignUpBody body) {
        String userId = body.getUserId();
        String email = body.getEmail();

        boolean userNameExists = userProfileRepository.existsById(userId);
        if(userNameExists)
            return new SignUpResponse("Username already exists", false);


        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
            return new SignUpResponse("Not a valid email format", false);

        boolean emailExists = userProfileRepository.existsByEmail(email);
        if(emailExists)
            return new SignUpResponse("Email already exists", false);

        String password = body.getPassword();
        Hash hash = Password.hash(password).withBCrypt();

        UserProfile userProfile = new UserProfile(userId, email, hash.getResult(), null);
        userProfileRepository.saveAndFlush(userProfile);
        User user = User.builder()
                .userNameId(userId)
                .email(email)
                .followedUsers(new HashMap<>())
                .followedChannels(new HashMap<>())
                .blockedUsers(new HashMap<>())
                .reportedUsers(new HashMap<>())
                .build();
        userRepository.save(user);

        return new SignUpResponse("Registration done successfully", true);
    }
}
