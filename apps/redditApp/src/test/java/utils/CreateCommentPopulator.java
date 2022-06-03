package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostCommentHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import mocks.CommentMock;
import mocks.PostMock;
import mocks.UserMock;

public class CreateCommentPopulator {
    public static void populate(UserRepository userRepository, PostRepository postRepository) {
        userRepository.save(UserMock.getUserFollowsChannel(PostMock.getChannelId()));
        postRepository.save(PostMock.getPost());

    }

    public static void clear(UserRepository userRepository, PostRepository postRepository,
                             CommentRepository commentRepository, PostCommentHierarchyRepository postCommentHierarchyRepository) {
        userRepository.deleteById(UserMock.getId());
        postRepository.deleteById(PostMock.getId());
        commentRepository.deleteById(CommentMock.getId());
        postCommentHierarchyRepository.deleteById(PostMock.getId() + CommentMock.getId());

    }
}
