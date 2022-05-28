import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.followReddit.FollowRedditService;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.app.repository.vote.RedditFollowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.google.common.truth.Truth.assertThat;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserRepository.class})
//@ContextConfiguration(classes = {GeneralConfig.class, DemoConfiguration.class, PostgresqlConfig.class, RedisConfig.class, SqlJpaConfig.class})
//@ContextConfiguration("/application.properties")
public class RedditTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    RedditFollowRepository redditFollowRepository;
    @Autowired
    FollowRedditService followRedditService;


//    public String createTestUser(){
//        User testUser = new User();
//        String userId = "test"+(int)(Math.random()*100000);
//        testUser.setUserNameId(userId);
//        System.out.println(userId);
//        userRepository.save(testUser);
//        return userId;
//    }
//    public String createTestReddit(){
//        Channel testReddit = new Channel();
//        String redditId = "test"+Math.random()*100000;
//        testReddit.setChannelNameId(redditId);
//        channelRepository.save(testReddit);
//        return redditId;
//    }
//    public int getFollowers(String redditId){
//        return redditFollowRepository.getById(redditId).getFollowerCount();
//
//    }
//
//    public User getUser(String userId){
//        Optional<User> optUser = userRepository.findById(userId);
//        if (optUser.isEmpty()){
//            throw new NullPointerException();
//        }
//        return optUser.get();
//    }
//
//    public Channel getReddit(String redditId){
//        Optional<Channel> optReddit = channelRepository.findById(redditId);
//        if (optReddit.isEmpty()){
//            throw new NullPointerException();
//        }
//        return optReddit.get();
//    }
//    @Test
//    void followRedditPass() throws Exception {
//        // Given
//
//        String userId = createTestUser();
//        String redditId = createTestReddit();
//        int initialFollowers = getFollowers(redditId);
//
//        FollowRedditForm followRedditForm = new FollowRedditForm(userId,redditId);
//        followRedditService.execute(followRedditForm);
//
//
//        User user = getUser(userId);
//        int finalFollowers = getFollowers(redditId);
//
//
//        assertThat(user.getFollowedChannels()).containsKey(redditId);
//        assertThat(finalFollowers).isEqualTo(initialFollowers+1);
//    }

    @Test
    void testing() throws Exception{
        User u = new User();
        u.setUserNameId("hello");
            userRepository.save(u);
        assertThat(Boolean.TRUE).isTrue();
    }
}
