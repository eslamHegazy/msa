package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import mocks.CreateChannelFormMock;
import mocks.UserMock;

public class CreateChannelPopulator {
    public static void populate(UserRepository userRepository) {
        userRepository.save(UserMock.getUser());

    }

    public static void clear(UserRepository userRepository, ChannelRepository channelRepository){
        userRepository.deleteById(UserMock.getId());
        channelRepository.deleteById(CreateChannelFormMock.getChannelNameId());
    }
}
