package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.repository.PostRepository;
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
    public ReportPostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Object execute(Object body) throws Exception {

        HashMap<String,String> requestBody = (HashMap<String,String>) body;
        String userId = requestBody.get("userId");
        String postId = requestBody.get("postId");
        String reason = requestBody.get("reason");

        Optional<Post> post = postRepository.findById(postId);

        if (!post.isPresent()){
            throw new IllegalStateException("Post with Id: "+ postId + "does not exist in the database");
        }
        HashMap<String, String> report = new HashMap<String, String>();
        report.put(userId,reason);
        try {
            postRepository.addReport(postId, report);
        }
        catch (Exception e){
            return e.getMessage();
        }

        return "report added successfully";
    }
}
