package com.ScalableTeam.reddit.app.reportPost;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.requestForms.ReportPostForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class ReportPostService implements MyCommand {

    @Autowired
    private final ChannelRepository channelRepository;
    @Autowired
    private final PostRepository postRepository;

    @Autowired
    public ReportPostService(ChannelRepository channelRepository, PostRepository postRepository) {
        this.channelRepository =channelRepository;
        this.postRepository = postRepository;
    }

    @Override
    @CachePut("postsCache")
    public String execute(Object body) throws Exception {

ReportPostForm request =  (ReportPostForm) body;
        String userId = request.getUserId();
        String postId = request.getPostId();
try {
        Optional<Post> post = postRepository.findById(postId);

        if (!post.isPresent()){
            throw new IllegalStateException("Post with Id: "+ postId + "does not exist in the database");
        }
        String redditId = post.get().getChannelId();
        Optional<Channel> reddit = channelRepository.findById(redditId);

        if(!reddit.isPresent()){
            throw new IllegalStateException("Channel with Id: "+ redditId + "does not exist in the database");
        }

        HashMap<String, String> report = new HashMap<String, String>();
        report.put(postId,userId);
        try {
            channelRepository.addReport(postId, report);
        }
        catch (Exception e){
            return e.getMessage();
        }

        return "report added successfully";}
catch (Exception e){
    throw e;
}
    }
}
