package com.ScalableTeam.reddit.app.caching;

import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.RedditFollowersEdgeRepository;
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
    @Autowired
    private RedditFollowersEdgeRepository redditFollowersEdgeRepository;
    @Autowired
    private ChannelRepository channelRepository;

    public static byte[] serialize(Object obj) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ;
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            bytes = baos.toByteArray();
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static Object deSerialize(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @CachePut(cacheNames = "popularPostsCache", key = "#postId")
    public String updatePopularPostsCache(String postId, Post post) {

        System.err.println("updating " + post);
        return post.toString();
    }

    @CachePut(cacheNames = "postsCache", key = "#postId")
    public String updatePostsCache(String postId, Post post) {
        return post.toString();
    }

    @CachePut(cacheNames = "popularChannelsCache", key = "#redditId")
    public String updatePopularChannelsCache(String redditId, Channel channel) {
        return channel.toString();
    }

    @CacheEvict(cacheNames = "popularChannelsCache", key = "#redditId")
    public void removePreviouslyPopularChannel(String redditId) {
    }

    @CacheEvict(cacheNames = "popularPostsCache", key = "#postId")
    public void removePreviouslyPopularPost(String postId) {
    }

    @CacheEvict(cacheNames = "postsCache", key = "#userNameId")
    public void removeWallFromCache(String userNameId) {
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
    private Post[] getPostsFromFollowedChannels(String newLatestReadPostId, HashMap<String, Boolean> followedChannels) {
        return postRepository.getPostsByTimeAndChannel(newLatestReadPostId, followedChannels);
    }

    @Cacheable(cacheNames = "postsCache")
    private Post[] getPostsFromFollowedUsers(String newLatestReadPostId, HashMap<String, Boolean> followedUsers) {
        return postRepository.getPostsByTimeAndUser(newLatestReadPostId, followedUsers);
    }

    @Cacheable(cacheNames = "postsCache", key = "#userNameId")
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

    @Cacheable(cacheNames = "recommendationsCache", key = "#channelNameId", condition = "#channelNameId!=null")
    public String getRecommendations(String redditId) throws Exception {
        try {
            System.out.println("cache service");
            Channel reddit = channelRepository.findById(redditId).get();
            ArrayList<User> fols = (ArrayList<User>) reddit.getFollowers().getEntity();

            HashMap<String, Integer> frequencies = new HashMap<>();
            for (User u : fols) {
                HashMap<String, Boolean> channels = u.getFollowedChannels();
                if (channels == null) {
                    continue;
                }
                for (String ch : channels.keySet()) {
                    if (frequencies.containsKey(ch)) {
                        frequencies.replace(ch, frequencies.get(ch) + 1);
                    } else {
                        frequencies.put(ch, 1);
                    }

                }
            }

            System.out.println("freqs " + frequencies);
            return frequencies.toString();

        } catch (Exception e) {

            return "";
        }
    }
}
