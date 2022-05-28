package config;

import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestBeansConfig {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @Bean
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Bean
    public PostRepository getPostRepository() {
        return postRepository;
    }
}
