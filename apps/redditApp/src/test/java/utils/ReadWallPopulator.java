package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import mocks.PostMock;
import mocks.UserMock;


public class ReadWallPopulator {

    public static void populate(UserRepository userRepository, PostRepository postRepository) {
        userRepository.save(UserMock.getUserFollowsChannel(PostMock.getChannelId()));
        postRepository.save(PostMock.getPost());


    }

    public static void clear(UserRepository userRepository, PostRepository postRepository, CachingService cachingService) {
        userRepository.deleteById(UserMock.getId());
        postRepository.deleteById(PostMock.getId());
        cachingService.removeWallFromCache(UserMock.getId());


    }
}
