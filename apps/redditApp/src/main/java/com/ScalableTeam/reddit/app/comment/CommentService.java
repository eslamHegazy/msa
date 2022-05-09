package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import java.util.Optional;
@ComponentScan("com.ScalableTeam.reddit")
@Service
public class CommentService implements MyCommand {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    public CommentService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    @Override
    public String execute(Object body) {
        try{
//            CommentResponseForm commentResponseForm=(CommentResponseForm) body;
//            Comment comment=commentResponseForm.getComment();
            Comment comment=(Comment)body;
            final Optional<User> postCreatorOptional = userRepository.findById(comment.getUserNameId());
            final String channelId=postRepository.getChannelOfPost(comment.getCommentParentId());


            if (postCreatorOptional.isEmpty() ||
                    !postCreatorOptional.get().getFollowedChannels().containsKey(channelId)) {
                return "invalid Action";
            }
            commentRepository.save(comment);
            HashMap<String,Comment>hm=new HashMap<>();
            //Comment Id is generated and set by save of commentRepository
            hm.put(comment.getId(), comment);
            postRepository.updateFieldInPost(comment.getCommentParentId(), "comments",hm);
            return "Comment added Successfully";
        }
        catch (Exception e){
            return "Error: Couldn't add comment";
        }
    }
}
