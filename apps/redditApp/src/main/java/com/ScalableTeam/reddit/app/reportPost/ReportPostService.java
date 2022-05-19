package com.ScalableTeam.reddit.app.reportPost;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.requestForms.ReportPostForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class ReportPostService implements MyCommand {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final ChannelRepository channelRepository;

    @Autowired
    public ReportPostService(PostRepository postRepository, ChannelRepository channelRepository) {
        this.postRepository = postRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public String execute(Object body) throws Exception {

        try{
        ReportPostForm request = (ReportPostForm) body;
        String userId = request.getUserId();
        String postId = request.getPostId();

        Optional<Post> post = postRepository.findById(postId);

        if (!post.isPresent()){
            throw new IllegalStateException("Post with Id: "+ postId + "does not exist in the database");
        }
        Post thread = post.get();
        if(thread.getChannelId()==null){
            throw new IllegalStateException("Post with Id: "+ postId + "has no channelId");
        }
        HashMap<String, String> report = new HashMap<String, String>();
        report.put(postId,userId);

        channelRepository.addReport(postId, report);
        return "report added successfully";}

        catch (Exception e){
            return e.getMessage();
        }
    }
}
