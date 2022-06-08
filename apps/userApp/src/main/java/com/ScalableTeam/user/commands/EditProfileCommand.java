package com.ScalableTeam.user.commands;

import com.ScalableTeam.models.user.EditProfileBody;
import com.ScalableTeam.models.user.EditProfileResponse;
import com.ScalableTeam.models.user.SignUpResponse;
import com.ScalableTeam.user.entity.UserProfile;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class EditProfileCommand implements ICommand<EditProfileBody, EditProfileResponse> {

    private final UserProfileRepository userProfileRepository;

    @Override
    public EditProfileResponse execute(EditProfileBody body) {
        if (!userProfileRepository.existsById(body.getUserId()))
            return new EditProfileResponse(false, "This user id does not exist");
        UserProfile userProfile = userProfileRepository.findById(body.getUserId()).get();
        String newEmail = body.getNewEmail();

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(newEmail);
        if (!matcher.matches())
            return new EditProfileResponse(false, "Not a valid email format");

        boolean emailExists = userProfileRepository.existsByEmail(newEmail);
        if (emailExists)
            return new EditProfileResponse(false, "Email already exists");

        userProfile.setEmail(body.getNewEmail());
        userProfileRepository.saveAndFlush(userProfile);
        return new EditProfileResponse(true, "The profile was updated successfully");
    }
}
