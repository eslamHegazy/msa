package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.CommentVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVoteCommentRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import mocks.CommentMock;
import mocks.PostMock;
import mocks.UserMock;

public class CreatePostPopulator {
    public static void populate(UserRepository userRepository) {
        userRepository.save(UserMock.getUserFollowsChannel(PostMock.getChannelId()));

    }

    public static void clear(UserRepository userRepository, PostRepository postRepository){
        userRepository.deleteById(UserMock.getId());
        postRepository.deleteById(PostMock.getId());
    }
}
