package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;

import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;

import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
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
    @Autowired
    private GeneralConfig generalConfig;
    //    public CommentService(PostRepository postRepository) {
//        this.postRepository = postRepository;
//    }
    @Override
    public Post execute(Object body) throws Exception{
        log.info(generalConfig.getCommands().get("comment") + "Service", body);
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
            Post post=postParentOptional.get();
            return continueExecuting(comment,post,postId);
        } catch (Exception e) {

            throw new Exception("Invalid Action");
            //return "Error: Couldn't add comment";
        }
    }
    @CachePut(cacheNames = {"postsCache","popularPostsCache"},key = "#postId")
    public Post continueExecuting(Comment comment, Post post,String postId){
        commentRepository.save(comment);
        HashMap<String, Comment> hm = new HashMap<>();
        //Comment Id is generated and set by save of commentRepository
        //Update the post and return it to update cache
        hm.put(comment.getId(), comment);
        if(post.getComments()==null){
            post.setComments(hm);
        }
        else {
            hm=post.getComments();
            hm.put(comment.getId(), comment);
            post.setComments(hm);
        }
        postRepository.updateFieldInPost(postId, "comments", hm);
        return post;

    }
}
