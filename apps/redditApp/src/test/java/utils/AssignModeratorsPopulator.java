package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import mocks.ChannelMock2;
import mocks.CreateChannelFormMock;
import mocks.ModeratorMock;
import mocks.UserMock;

public class AssignModeratorsPopulator {
    public static void populate(UserRepository userRepository, ChannelRepository channelRepository) {
        userRepository.save(UserMock.getUserFollowsChannel(ChannelMock2.getChannelNameId()));
        userRepository.save(ModeratorMock.getModeratorFollowsChannel(ChannelMock2.getChannelNameId()));
        channelRepository.save(ChannelMock2.getChannelWithAdminAsModerator());

    }

    public static void clear(UserRepository userRepository, ChannelRepository channelRepository) {
        userRepository.deleteById(UserMock.getId());
        userRepository.deleteById(ModeratorMock.getId());
        channelRepository.deleteById(CreateChannelFormMock.getChannelNameId());
    }
}
