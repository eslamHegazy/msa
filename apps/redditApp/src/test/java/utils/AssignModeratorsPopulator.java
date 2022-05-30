package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import mocks.ChannelMock;
import mocks.CreateChannelFormMock;
import mocks.ModeratorMock;
import mocks.UserMock;

public class AssignModeratorsPopulator {
    public static void populate(UserRepository userRepository,ChannelRepository channelRepository) {
        userRepository.save(UserMock.getUserFollowsChannel(ChannelMock.getChannelNameId()));
        userRepository.save(ModeratorMock.getModeratorFollowsChannel(ChannelMock.getChannelNameId()));
        channelRepository.save(ChannelMock.getChannelWithAdminAsModerator());

    }

    public static void clear(UserRepository userRepository, ChannelRepository channelRepository){
        userRepository.deleteById(UserMock.getId());
        userRepository.deleteById(ModeratorMock.getId());
        channelRepository.deleteById(CreateChannelFormMock.getChannelNameId());
    }
}
