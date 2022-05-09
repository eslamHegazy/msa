package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.reddit.app.entity.Post;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.HashMap;


public interface PostRepository extends ArangoRepository<Post, String> {
    @Query("FOR p IN posts FILTER p._key == @key RETURN p.channelId")
    String getChannelOfPost(@Param("key") String key);
    @Query("FOR p IN posts FILTER  (p.time >= @earliestTime OR p.time<=@latestTime) AND HAS(@followedChannels,p.channelId) LIMIT 25 RETURN p")
    Post[] getPostsByTimeAndChannel(@Param("earliestTime") Date earliestTime, @Param("latestTime") Date latestTime, @Param("followedChannels")HashMap<String,Boolean> followedChannels);
    @Query("FOR p IN posts FILTER (p.time >= @earliestTime OR p.time<=@latestTime) AND HAS(@followedUsers,p.userNameId) LIMIT 25 RETURN p")
    Post[] getPostsByTimeAndUser(@Param("earliestTime") Date earliestTime,@Param("latestTime") Date latestTime,@Param("followedUsers") HashMap<String,Boolean> followedUsers);
    @Query("FOR p IN posts UPDATE {_key:@key,@fieldName:@fieldValue} IN posts")
    void updateFieldInPost(@Param("key") String key, @Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);

}
