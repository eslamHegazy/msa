import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.BanUserForm;
import com.ScalableTeam.models.reddit.FollowRedditForm;
import com.ScalableTeam.models.reddit.ReportPostForm;
import com.ScalableTeam.models.reddit.ViewReportsForm;
import com.ScalableTeam.reddit.RedditApplication;
import com.ScalableTeam.reddit.app.entity.vote.RedditFollowers;
import com.ScalableTeam.reddit.app.followReddit.FollowRedditService;
import com.ScalableTeam.reddit.app.followReddit.UnfollowRedditService;
import com.ScalableTeam.reddit.app.moderation.BanUserService;
import com.ScalableTeam.reddit.app.moderation.ViewReportsService;
import com.ScalableTeam.reddit.app.recommendations.RedditsRecommendationsService;
import com.ScalableTeam.reddit.app.reportPost.ReportPostService;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.RedditFollowRepository;
import config.TestBeansConfig;
import mocks.ChannelMock;
import mocks.PostMock;
import mocks.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static com.google.common.truth.Truth.assertThat;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserRepository.class})
//@ContextConfiguration(classes = {GeneralConfig.class, DemoConfiguration.class, PostgresqlConfig.class, RedisConfig.class, SqlJpaConfig.class})
//@ContextConfiguration("/application.properties")
@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = RedditApplication.class)
public class RedditTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    RedditFollowRepository redditFollowRepository;
    @Autowired
    FollowRedditService followRedditService;
    @Autowired
    ReportPostService reportPostService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    BanUserService banUserService;
    @Autowired
    ViewReportsService viewReportsService;
    @Autowired
    UnfollowRedditService unfollowRedditService;
    @Autowired
    RedditsRecommendationsService redditsRecommendationsService;
    public String createTestUser(){
        String userId = "test"+(int)(Math.random()*100000);
        User testUser = UserMock.getUserWithId(userId);
        System.out.println(userId);
        userRepository.save(testUser);
        return userId;
    }
    public String createTestReddit(){
        Channel testReddit = new Channel();
        String redditId = "test"+Math.random()*100000;
        testReddit.setChannelNameId(redditId);
        channelRepository.save(testReddit);
        return redditId;
    }
    public int getFollowers(String redditId){
        return redditFollowRepository.getById(redditId).getFollowerCount();

    }

    public User getUser(String userId){
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()){
            throw new NullPointerException();
        }
        return optUser.get();
    }

    public Channel getReddit(String redditId){
        Optional<Channel> optReddit = channelRepository.findById(redditId);
        if (optReddit.isEmpty()){
            throw new NullPointerException();
        }
        return optReddit.get();
    }
    @Test
    void followRedditArango() throws Exception {

        String userId = createTestUser();
        String redditId = createTestReddit();

        FollowRedditForm followRedditForm = new FollowRedditForm(userId,redditId);
        followRedditService.execute(followRedditForm);

        User user = getUser(userId);


        assertThat(user.getFollowedChannels()).containsKey(redditId);
    }
//    @Test
//    void followRedditSQL() throws Exception {


//        String redditId = "testReddit123";
//        Optional<RedditFollowers> optRedditFollowers= redditFollowRepository.findById(redditId);
//        if (optRedditFollowers.isPresent()){
//        redditFollowRepository.deleteById(redditId);
//        }
//        Optional<Channel> optChannel= channelRepository.findById(redditId);
//        if (optChannel.isPresent()){
//            channelRepository.deleteById(redditId);
//        }
//        String userId = "user"+(int)(Math.random()*100000);
//        User u = UserMock.getUserWithId(userId);
//        userRepository.save(u);
//        Channel ch = ChannelMock.getChannelWithId(redditId);
//        channelRepository.save(ch);
//
//        FollowRedditForm followRedditForm = new FollowRedditForm(userId,redditId);
//        followRedditService.execute(followRedditForm);
//
//        int finalFollowers =  redditFollowRepository.getById(redditId).getFollowerCount();
//        System.out.println(finalFollowers);
//
//        assertThat(finalFollowers).isEqualTo(1);
//    }

    @Test
    void unfollowRedditPass() throws Exception {

        String userId = "user"+(int)(Math.random()*100000);
        String redditId = "channel"+(int)(Math.random()*100000);
        User user = UserMock.getUserWithId(userId);
        Channel channel = ChannelMock.getChannelWithId(redditId);

        HashMap<String, Boolean> follow = new HashMap<String, Boolean>();
        follow.put(redditId,true);
        user.setFollowedChannels(follow);

        userRepository.save(user);
        channelRepository.save(channel);

        FollowRedditForm followRedditForm = new FollowRedditForm(userId,redditId);
        unfollowRedditService.execute(followRedditForm);

//        int finalFollowers = getFollowers(redditId);
        User retrievedUser  = userRepository.findById(userId).get();
        assertThat(retrievedUser.getFollowedChannels().containsKey(redditId)).isEqualTo(false);
//        assertThat(finalFollowers).isEqualTo(initialFollowers+1);

    }
    @Test
    void getRecommendations1() throws Exception {

        ArrayList<String> users = new ArrayList<String>();
        for (int i=0;i<16;i++){
            users.add(createTestUser());
        }
        User mainUser = userRepository.findById(users.get(15)).get();
        HashMap<String,Boolean> foll = new HashMap<String,Boolean>();
        for (String id: users){
            foll.put(id,true);
        }
        mainUser.setFollowedUsers(foll);
        userRepository.save(mainUser);
        ArrayList<String> channels = new ArrayList<String>();
        for (int i=0;i<5;i++){
            channels.add(createTestReddit());
        }
//        int counter=0;
        for (int i=5;i<0;i++){
            for(int j=0;j<i;j++){
            FollowRedditForm followRedditForm = new FollowRedditForm(users.get(j), channels.get(5-i));
            followRedditService.execute(followRedditForm);
            }
//            counter++;
        }
        for (String u :users){
            System.out.println(userRepository.findById(u).get().getFollowedChannels());
        }

        String[] recs = redditsRecommendationsService.execute(mainUser.getUserNameId());

        System.out.println(recs.toString());
        assertThat(recs).isEqualTo(List.of(channels.get(4), channels.get(3),channels.get(2), channels.get(1),channels.get(0)));

    }

    @Test
    void reportPostPass() throws Exception {
        String adminId ="admin"+(int)(Math.random()*100000);
        String channelId = "channel"+(int)(Math.random()*100000);
        String reporterId = "user"+(int)(Math.random()*100000);
        String postId = "post"+(int)(Math.random()*100000);
        User admin = UserMock.getUserWithId(adminId);
        User reporter = UserMock.getUserWithId(reporterId);
        Channel ch = ChannelMock.getChannelWithAdminChannelId(channelId, adminId);
        Post p = PostMock.getPostWithIdChannelId(postId,channelId);
        userRepository.save(admin);
        userRepository.save(reporter);
        channelRepository.save(ch);
        postRepository.save(p);
        ReportPostForm reportPostForm  = new ReportPostForm(reporterId, postId);
        reportPostService.execute(reportPostForm);

        Optional<Channel> retrievdChannel = channelRepository.findById(channelId);
        assertThat(retrievdChannel.get().getReports()).containsKey(reportPostForm.toString());
    }
    @Test
    void banUser1() throws Exception {
        String adminId ="admin"+(int)(Math.random()*100000);
        String channelId = "channel"+(int)(Math.random()*100000);
        String reporterId = "user"+(int)(Math.random()*100000);
        User admin = UserMock.getUserWithId(adminId);
        User reporter = UserMock.getUserWithId(reporterId);
        Channel ch = ChannelMock.getChannelWithAdminChannelId(channelId, adminId);
        System.out.println(channelId);
        System.out.println(reporterId);
        HashMap<String ,Boolean> mod = new HashMap<String, Boolean>();
        mod.put(adminId, true);
        ch.setModerators(mod);
        HashMap<String ,Boolean> follow = new HashMap<String, Boolean>();
        follow.put(channelId, true);
        reporter.setFollowedChannels(follow);
        userRepository.save(admin);
        userRepository.save(reporter);
        channelRepository.save(ch);

        BanUserForm banUserForm = new BanUserForm(adminId,reporterId,channelId);
        banUserService.execute(banUserForm);

        assertThat(userRepository.findById(reporterId).get().getFollowedChannels().containsKey(channelId)).isEqualTo(false);

    }
    @Test
    void banUser2() throws Exception {
        String adminId ="admin"+(int)(Math.random()*100000);
        String channelId = "channel"+(int)(Math.random()*100000);
        String reporterId = "user"+(int)(Math.random()*100000);
        User admin = UserMock.getUserWithId(adminId);
        User reporter = UserMock.getUserWithId(reporterId);
        Channel ch = ChannelMock.getChannelWithAdminChannelId(channelId, adminId);
        System.out.println(channelId);
        System.out.println(reporterId);
        HashMap<String ,Boolean> mod = new HashMap<String, Boolean>();
        mod.put(adminId, true);
        ch.setModerators(mod);
        HashMap<String ,Boolean> follow = new HashMap<String, Boolean>();
        follow.put(channelId, true);
        reporter.setFollowedChannels(follow);
        userRepository.save(admin);
        userRepository.save(reporter);
        channelRepository.save(ch);

        BanUserForm banUserForm = new BanUserForm(adminId,reporterId,channelId);
        banUserService.execute(banUserForm);

        assertThat(channelRepository.findById(channelId).get().getBannedUsers()).containsKey(reporterId);

    }
//    @Test
//    void removeFollowedChannelFromUser() throws Exception {
//        String adminId ="admin"+(int)(Math.random()*100000);
//        String channelId = "channel"+(int)(Math.random()*100000);
//        String userId = "user"+(int)(Math.random()*100000);
//        User admin = UserMock.getUserWithId(adminId);
//        User user = UserMock.getUserWithId(userId);
//        Channel ch = ChannelMock.getChannelWithAdminChannelId(channelId, adminId);
//        HashMap<String, Boolean> followedChannels = new HashMap<String, Boolean>();
//        followedChannels.put(channelId,true);
//        user.setFollowedChannels(followedChannels);
//        userRepository.save(admin);
//        userRepository.save(user);
//        channelRepository.save(ch);
//        System.out.println(userRepository.findById(userId).get().toString());
//        HashMap<String, Boolean> initial = userRepository.findById(userId).get().getFollowedChannels();
//        System.out.println(userRepository.findById(userId).get().getFollowedChannels());
//
//        userRepository.removeFollowedChannelsWithID(userId,followedChannels);
//        HashMap<String, Boolean> after = userRepository.findById(userId).get().getFollowedChannels();
//
//        assertThat(initial).containsKey(channelId);
//        assertThat(after.containsKey(channelId)).isEqualTo(false);
//
//    }

    @Test
    void viewReports() throws Exception {
        //create channel and set mod
        String adminId ="admin"+(int)(Math.random()*100000);
        String channelId = "channel"+(int)(Math.random()*100000);
        User admin = UserMock.getUserWithId(adminId);
        Channel ch = ChannelMock.getChannelWithAdminChannelId(channelId, adminId);
        HashMap<String ,Boolean> mod = new HashMap<String, Boolean>();
        mod.put(adminId, true);
        ch.setModerators(mod);
        userRepository.save(admin);
        channelRepository.save(ch);

        Channel channel = channelRepository.findById(channelId).get();

        //create post and report it
        HashMap<String,Boolean> reports = new HashMap<String,Boolean>();
        for(int i=5;i>0;i--) {
            String reporterId = "user" + (int) (Math.random() * 100000);
            String postId = "post" + (int) (Math.random() * 100000);
            User reporter = UserMock.getUserWithId(reporterId);
            Post p = PostMock.getPostWithIdChannelId(postId, channelId);
            userRepository.save(reporter);
            postRepository.save(p);
            ReportPostForm reportPostForm  = new ReportPostForm(reporterId, postId);
            reports.put(reportPostForm.toString(), true);
        }
        channel.setReports(reports);
        channelRepository.save(channel);

        ViewReportsForm viewReportsForm =  new ViewReportsForm(adminId,channelId);
        String  result = viewReportsService.execute(viewReportsForm);
        System.out.println(result);
        assertThat(result).isEqualTo(reports.toString());


    }


}
