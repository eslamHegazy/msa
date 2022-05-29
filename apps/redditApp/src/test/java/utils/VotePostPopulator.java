package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import mocks.PostMock;
import mocks.UserMock;

public class VotePostPopulator {

    public static void populate(UserRepository userRepository, PostRepository postRepository) {
        userRepository.save(UserMock.getUser());
        userRepository.save(UserMock.getUserWithId(UserMock.getId() + "Voter"));
        postRepository.save(PostMock.getPostWithUserNameId(UserMock.getId()));
    }

    public static void clear(UserRepository userRepository, PostRepository postRepository, UserVotePostRepository userVotePostRepository, PostVoteRepository postVoteRepository){
        userRepository.delete(UserMock.getUser());
        userRepository.delete(UserMock.getUserWithId(UserMock.getId() + "Voter"));
        postRepository.delete(PostMock.getPostWithUserNameId(UserMock.getId()));
        userVotePostRepository.deleteAll();
        postVoteRepository.deleteAll();
    }
}
