package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.VotePostForm;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.post.UpvotePostService;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import com.ScalableTeam.reddit.config.PopularityConfig;
import mocks.PostMock;
import mocks.UserMock;
import org.springframework.context.ApplicationContext;

public class GetPopularPostsPopulater {


    public static void populate(UserRepository userRepository, PostRepository postRepository, PopularityConfig popularityConfig, ApplicationContext context) throws Exception {
        userRepository.save(UserMock.getUserFollowsChannel(PostMock.getChannelId()));
        postRepository.save(PostMock.getPost());
        int popularPostsUpvoteThreshold = popularityConfig.getPostsUpvoteThreshold();
        for (int i = 0; i < popularPostsUpvoteThreshold; i++) {
            String voterId = UserMock.getId() + "Voter" + i;
            String postId = PostMock.getId();
            userRepository.save(UserMock.getUserWithIdFollowsChannel(voterId, PostMock.getChannelId()));
            VotePostForm votePostForm = VotePostForm
                    .builder()
                    .postId(postId)
                    .userNameId(voterId)
                    .build();
            UpvotePostService command = context.getBean(UpvotePostService.class);
            command.execute(votePostForm);
        }

    }

    public static void clear(UserRepository userRepository, PostRepository postRepository,
                             CachingService cachingService, UserVotePostRepository userVotePostRepository,
                             PostVoteRepository postVoteRepository, PopularityConfig popularityConfig) {
        userRepository.deleteById(UserMock.getId());
        postRepository.deleteById(PostMock.getId());
        cachingService.removePreviouslyPopularPost(PostMock.getId());
        int popularPostsUpvoteThreshold = popularityConfig.getPostsUpvoteThreshold();
        for (int i = 0; i < popularPostsUpvoteThreshold; i++) {
            String voterId = UserMock.getId() + "Voter" + i;
            userRepository.deleteById(voterId);
        }
        userVotePostRepository.deleteAll();
        postVoteRepository.deleteAll();

    }
}
