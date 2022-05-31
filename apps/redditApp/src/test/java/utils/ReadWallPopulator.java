package utils;

import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostCommentHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.CommentVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVoteCommentRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import com.ScalableTeam.reddit.config.PopularityConfig;
import mocks.CommentMock;
import mocks.PostMock;
import mocks.UserMock;
import org.springframework.beans.factory.annotation.Autowired;


public class ReadWallPopulator {

    public static void populate(UserRepository userRepository,PostRepository postRepository) {
        userRepository.save(UserMock.getUserFollowsChannel(PostMock.getChannelId()));
        postRepository.save(PostMock.getPost());


    }

    public static void clear(UserRepository userRepository, PostRepository postRepository, CachingService cachingService){
        userRepository.deleteById(UserMock.getId());
        postRepository.deleteById(PostMock.getId());
        cachingService.removeWallFromCache(UserMock.getId());



    }
}
