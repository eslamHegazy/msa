package com.ScalableTeam.reddit.app.caching;

import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Service
public class CachingService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
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
    public String updatePopularPostsCache(String postId, Post post){

        System.err.println("updating "+post);return post.toString();
    }
    @CachePut(cacheNames = "postsCache",key="#postId")
    public String updatePostsCache(String postId, Post post){
        return post.toString();
    }
    @CacheEvict(cacheNames = "popularPostsCache", key = "#postId")
    public void removePreviouslyPopularPost(String postId) {
    }
    @CacheEvict(cacheNames = "postsCache", allEntries = true)
    public void evictAllEntriesOfPostsCache() {
    }
    @CacheEvict(cacheNames = "popularPostsCache", allEntries = true)
    public void evictAllEntriesOfPopularPostsCache() {
    }
    @CacheEvict(cacheNames = "popularChannelsCache", allEntries = true)
    public void evictAllEntriesOfPopularChannelsCache() {
    }
    @Cacheable(cacheNames = "postsCache")
    private Post[]getPostsFromFollowedChannels(String newLatestReadPostId, HashMap<String,Boolean> followedChannels){
        return postRepository.getPostsByTimeAndChannel(newLatestReadPostId, followedChannels);
    }
    @Cacheable(cacheNames = "postsCache")
    private Post[]getPostsFromFollowedUsers(String newLatestReadPostId,HashMap<String,Boolean>followedUsers){
        return postRepository.getPostsByTimeAndUser(newLatestReadPostId,followedUsers);
    }
    @Cacheable(cacheNames = "postsCache")
    public String getWall(String userNameId) throws Exception {
        try {

            final Optional<User> userOptional = userRepository.findById(userNameId);

            if (userOptional.isEmpty()) {
                throw new Exception();
            }
            User user = userOptional.get();
            //FIND NEW TOP 25 POSTS WHERE CHANNEL IN FOLLOWED CHANNELS
            //FIND NEW TOP OF 25 POSTS WHERE USER IN FOLLOWED USERS
//            Date earliestTime=user.getEarliestTime()==null?Date.from(Instant.now()):user.getEarliestTime();
//            Date latestTime=user.getLatestTime()==null?Date.from(Instant.now()):user.getLatestTime();
            String newLatestReadPostId = user.getLatestReadPostId() == null ? "" : user.getLatestReadPostId();
            Post[] feedFromChannels = postRepository.getPostsByTimeAndChannel(newLatestReadPostId, user.getFollowedChannels());
            ;
            Post[] feedFromUsers = postRepository.getPostsByTimeAndUser(newLatestReadPostId, user.getFollowedUsers());
            Post[] feedTotal = new Post[feedFromUsers.length + feedFromChannels.length];

            for (int i = 0; i < feedFromChannels.length; i++) {
                feedTotal[i] = feedFromChannels[i];
//                Instant time=(feedTotal[i].getTime());
//                if(time==null)
//                    time=Instant.now();
//                Date date=Date.from(time);
//                if(earliestTime.compareTo(date)<0){
//                   earliestTime=date;
//                }
//                if(latestTime.compareTo(date)>0){
//                   latestTime=date;
//                }
                if (feedTotal[i].getId().compareTo(newLatestReadPostId) > 0) {
                    newLatestReadPostId = feedTotal[i].getId();
                }
            }
            for (int i = 0; i < feedFromUsers.length; i++) {
                feedTotal[i + feedFromChannels.length] = feedFromUsers[i];
//                Instant time=(feedTotal[i].getTime());
//                if(time==null)
//                    time=Instant.now();
//                Date date=Date.from(time);
//                if(earliestTime.compareTo(date)<0){
//                    earliestTime=date;
//                }
//                if(latestTime.compareTo(date)>0){
//                    latestTime=date;
//                }
                if (feedTotal[i + feedFromChannels.length].getId().compareTo(newLatestReadPostId) > 0) {
                    newLatestReadPostId = feedTotal[i].getId();
                }
            }
//            user.setEarliestTime(earliestTime);
//            user.setLatestTime(latestTime);
            user.setLatestReadPostId(newLatestReadPostId);
            userRepository.save(user);

            ArrayList<Post> feedArray = new ArrayList<Post>();
            for (Post p : feedTotal) {
                feedArray.add(p);
            }
            return feedArray.toString();
//            return feedTotal.toString() ;
//            return feedTotal;
        } catch (Exception e) {

            throw new Exception("Exception When getting the feed");
        }
    }
}
