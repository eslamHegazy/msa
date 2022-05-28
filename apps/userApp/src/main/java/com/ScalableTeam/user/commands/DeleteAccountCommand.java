package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.DeleteAccountBody;
import com.ScalableTeam.models.user.DeleteAccountResponse;
import com.ScalableTeam.user.repositories.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class DeleteAccountCommand implements ICommand<DeleteAccountBody, DeleteAccountResponse> {
    final private UserProfileRepository userProfileRepository;
    final private UserRepository userRepository;

    @Override
    public DeleteAccountResponse execute(DeleteAccountBody body) {
        String userId = body.getUserId();
        if(!userProfileRepository.existsById(userId))
            return new DeleteAccountResponse(false, "User does not exist");
        userProfileRepository.deleteById(userId);
        userRepository.deleteById(userId);

        //TODO: delete the profile picture
        return new DeleteAccountResponse(true, "Deleted successfully");
    }
}
