package com.ScalableTeam.user.commands;

import com.ScalableTeam.models.user.EditProfileBody;
import com.ScalableTeam.models.user.EditProfileResponse;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EditProfileCommand implements ICommand<EditProfileBody, EditProfileResponse>{

    private final UserProfileRepository userProfileRepository;
    @Override
    public EditProfileResponse execute(EditProfileBody body) {
        if (!userProfileRepository.existsById(body.getUserId()))
            return new EditProfileResponse(false, "This user id does not exist");
        userProfileRepository.getById(body.getUserId()).setEmail(body.getNewEmail());
        return new EditProfileResponse(true, "The profile was updated successfully");
    }
}
