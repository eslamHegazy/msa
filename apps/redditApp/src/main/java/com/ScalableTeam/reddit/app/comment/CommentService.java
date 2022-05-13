package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;

import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class CommentService implements MyCommand {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Value("#{${commands}}")
    private Map<String, String> commands;
    //    public CommentService(PostRepository postRepository) {
//        this.postRepository = postRepository;
//    }
    @Override
    public String execute(Object body) throws Exception{
        log.info(commands.get("comment") + "Service", body);
        try {
//            CommentResponseForm commentResponseForm=(CommentResponseForm) body;
//            Comment comment=commentResponseForm.getComment();
            Comment comment = (Comment) body;

            final Optional<User> postCreatorOptional = userRepository.findById(comment.getUserNameId());
            String postId = comment.getPostId() == null ? comment.getCommentParentId() : comment.getPostId();
            final Optional<Post> postParentOptional = postRepository.findById(postId);
//            final String channelId=postRepository.getChannelOfPost(comment.getCommentParentId());
            //user has to follow the channel
            if (postCreatorOptional.isEmpty()||postParentOptional.isEmpty()||
            !postCreatorOptional.get().getFollowedChannels().containsKey(postParentOptional.get().getChannelId())) {
                throw new Exception();
            }
            //check if comment parent belongs to the same post
            if (!comment.isCommentOnPost() &&
                    !postParentOptional.get().getComments().containsKey(comment.getCommentParentId())) {
                throw new Exception();

            }
            commentRepository.save(comment);
            HashMap<String, Comment> hm = new HashMap<>();
            //Comment Id is generated and set by save of commentRepository
            hm.put(comment.getId(), comment);
            postRepository.updateFieldInPost(postId, "comments", hm);
            return "Comment added Successfully";
        } catch (Exception e) {

            throw new Exception("Invalid Action");
            //return "Error: Couldn't add comment";
        }
    }
}
