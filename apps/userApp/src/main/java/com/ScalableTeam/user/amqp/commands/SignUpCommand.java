package com.ScalableTeam.user.amqp.commands;

import com.password4j.Hash;
import com.password4j.Password;
import com.ScalableTeam.user.amqp.entity.UserProfile;
import lombok.AllArgsConstructor;
import models.SignUpBody;
import models.SignUpResponse;
import org.springframework.stereotype.Service;
import com.ScalableTeam.user.amqp.repositories.UserProfileRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class SignUpCommand implements ICommand<SignUpBody, SignUpResponse> {

    private final UserProfileRepository userProfileRepository;
    @Override
    public SignUpResponse execute(SignUpBody body) {
        boolean userNameExists = userProfileRepository.existsById(body.getUserId());
        if(userNameExists)
            return new SignUpResponse("Username already exists", false);

        String email = body.getEmail();
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

        UserProfile userProfile = new UserProfile(body.getUserId(), body.getEmail(), hash.getResult(), null);
        userProfileRepository.saveAndFlush(userProfile);

        return new SignUpResponse("Registration done successfully", true);
    }
}
