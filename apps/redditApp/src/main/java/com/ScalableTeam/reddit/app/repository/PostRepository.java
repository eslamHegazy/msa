package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.arango.Post;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;


public interface PostRepository extends ArangoRepository<Post, String> {
    @Query("FOR p IN posts FILTER p._key == @key RETURN p.channelId")
    String getChannelOfPost(@Param("key") String key);
    @Query("FOR p IN posts FILTER p._key > @latestReadPostId AND HAS(@followedChannels,p.channelId) LIMIT 25 RETURN p")
    Post[] getPostsByTimeAndChannel(@Param("latestReadPostId") String latestReadPostId, @Param("followedChannels")HashMap<String,Boolean> followedChannels);
    @Query("FOR p IN posts FILTER  p._key > @latestReadPostId AND HAS(@followedUsers,p.userNameId) LIMIT 25 RETURN p")
    Post[] getPostsByTimeAndUser(@Param("latestReadPostId") String latestReadPostId,@Param("followedUsers") HashMap<String,Boolean> followedUsers);
    @Query("FOR p IN posts UPDATE {_key:@key,@fieldName:@fieldValue} IN posts")
    void updateFieldInPost(@Param("key") String key, @Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);
    @Query("FOR p IN posts FILTER HAS(@channelId,p.channelId)")
    Post[] getPostsByChannel(@Param("followedChannels")String channelId);


}
