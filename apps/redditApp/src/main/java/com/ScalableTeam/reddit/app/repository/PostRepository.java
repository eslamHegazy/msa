package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;


public interface PostRepository extends ArangoRepository<Post, String> {
    @Query("FOR p IN posts FILTER p._key == @key RETURN p.channelId")
    String getChannelOfPost(@Param("key") String key);
    @Query("FOR p IN posts FILTER HAS(@followedChannels,p.channelId) AND p._key > @latestReadPostId LIMIT 25 RETURN p")
    Post[] getPostsByTimeAndChannel(@Param("latestReadPostId") String latestReadPostId, @Param("followedChannels")HashMap<String,Boolean> followedChannels);
    @Query("FOR p IN posts FILTER HAS(@followedUsers,p.userNameId) AND p._key > @latestReadPostId LIMIT 25 RETURN p")
    Post[] getPostsByTimeAndUser(@Param("latestReadPostId") String latestReadPostId,@Param("followedUsers") HashMap<String,Boolean> followedUsers);
    @Query("FOR p IN posts UPDATE {_key:@key,@fieldName:@fieldValue} IN posts")
    void updateFieldInPost(@Param("key") String key, @Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);
    @Query("FOR p IN posts FILTER HAS(@channelId,p.channelId)")
    Post[] getPostsByChannel(@Param("followedChannels")String channelId);


}
