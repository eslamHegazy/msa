package com.ScalableTeam.user.commands;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.user.BlockedUserBody;
import com.ScalableTeam.models.user.BlockedUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BlockUserCommand implements ICommand<BlockedUserBody, BlockedUserResponse> {
    private final UserRepository userRepository;

    @Override
    public BlockedUserResponse execute(BlockedUserBody body) {
        String userId = body.getUserId();
        String blockedUserId = body.getBlockedUserId();

        if (userId == blockedUserId) {
            return new BlockedUserResponse(false, "The user can not block himself/herself!");
        }

        Optional<User> blockedUser = userRepository.findById(blockedUserId);
        if (!blockedUser.isPresent()) {
            return new BlockedUserResponse(false, "The blocked user not found in DB!");
        }

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return new BlockedUserResponse(false, "User not found in DB!");
        }


        HashMap<String, Boolean> block = new HashMap<String, Boolean>();
        block.put(blockedUserId, true);
        userRepository.updateBlockedUsersWithID(userId, block);

        return new BlockedUserResponse(true, "User blocked successfully");
    }
}
