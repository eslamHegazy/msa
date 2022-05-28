package com.ScalableTeam.user.commands;

import com.ScalableTeam.models.user.UpdatePhotoBody;
import com.ScalableTeam.models.user.UpdatePhotoResponse;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdatePhotoCommand implements ICommand<UpdatePhotoBody, UpdatePhotoResponse>{

    private final UserProfileRepository userProfileRepository;
    @Override
    public UpdatePhotoResponse execute(UpdatePhotoBody body) {
        if (!userProfileRepository.existsById(body.getUserId()))
            return new UpdatePhotoResponse(false, "This user id does not exist");
        String oldProfilePhotoLink = userProfileRepository.getById(body.getUserId()).getProfilePhotoLink();
        if (oldProfilePhotoLink!=null) {
            // TODO: delete
        }
        userProfileRepository.getById(body.getUserId()).setProfilePhotoLink(body.getPhotoUrl());
        // TODO: check valid url / existing already/ sth like this ; may be skipped for convinience
        return new UpdatePhotoResponse(true, "The profile photo was updated successfully");
    }
}
