package utils;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import mocks.PostMock;
import mocks.UserMock;

public class SearchDataPopulater {
    private static final String[] titles = new String[]{
            "As he crossed toward the pharmacy at the corner",
            "Jacob stood on his tiptoes.",
            "The car turned the corner.",
            "Kelly twirled in circles.",
            "She opened the door.",
            "Aaron made a picture.",
            "My parents and I went to a movie.",
            "The paper and pencil sat idle on the desk.",
            "Jenny and I opened all the gifts.",
            "Sarah and Ira drove to the store.",
            "I rinsed and dried the dishes.",
    };

    public static void populate(UserRepository userRepository, PostRepository postRepository) {
        userRepository.save(UserMock.getUser());
        postRepository.saveAll(PostMock.getPostsWithTitle(titles));
    }

    public static void clear(UserRepository userRepository, PostRepository postRepository) {
        userRepository.delete(UserMock.getUser());
        postRepository.deleteAll(PostMock.getPostsWithTitle(titles));
    }
}
