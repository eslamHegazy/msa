package com.ScalableTeam.reddit.app.caching;

import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;

@Service
public class CachingService {
    @Autowired
    private PostRepository postRepository;
    public static byte[] serialize(Object obj){
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();;
            ObjectOutputStream oos=new ObjectOutputStream(baos);
            oos.writeObject(obj);
            bytes=baos.toByteArray();
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    public static Object deSerialize(byte[] bytes){
        Object obj=null;
        try {
            ByteArrayInputStream bais=new ByteArrayInputStream(bytes);
            ObjectInputStream ois=new ObjectInputStream(bais);
            obj=ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    @CachePut(cacheNames = "popularPostsCache",key="#postId")
    public Post updatePopularPostsCache(String postId, Post post){

        System.err.println("updating "+post);return post;
    }
    @CachePut(cacheNames = "postsCache",key="#postId")
    public Post updatePostsCache(String postId, Post post){
        return post;
    }
    @CacheEvict(cacheNames = "popularPostsCache", key = "#postId")
    public void removePreviouslyPopularPost(String postId) {
    }
    @Cacheable(cacheNames = "postsCache")
    private Post[]getPostsFromFollowedChannels(String newLatestReadPostId, HashMap<String,Boolean> followedChannels){
        return postRepository.getPostsByTimeAndChannel(newLatestReadPostId, followedChannels);
    }
    @Cacheable(cacheNames = "postsCache")
    private Post[]getPostsFromFollowedUsers(String newLatestReadPostId,HashMap<String,Boolean>followedUsers){
        return postRepository.getPostsByTimeAndUser(newLatestReadPostId,followedUsers);
    }
}
