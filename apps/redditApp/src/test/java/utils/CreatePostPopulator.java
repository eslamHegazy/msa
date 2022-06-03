package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import mocks.PostMock;
import mocks.UserMock;

public class CreatePostPopulator {
    public static void populate(UserRepository userRepository) {
        userRepository.save(UserMock.getUserFollowsChannel(PostMock.getChannelId()));

    }

    public static void clear(UserRepository userRepository, PostRepository postRepository) {
        userRepository.deleteById(UserMock.getId());
        postRepository.deleteById(PostMock.getId());
    }
}
