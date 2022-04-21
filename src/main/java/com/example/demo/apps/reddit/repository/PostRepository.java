package com.example.demo.apps.reddit.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import com.example.demo.apps.reddit.entity.Post;
import org.springframework.data.repository.query.Param;


public interface PostRepository extends ArangoRepository<Post, String> {
    @Query("FOR p IN posts FILTER p._key == @key RETURN p.channelId")
    String getChannelOfPost(@Param("key") String key);

    @Query("FOR p IN posts UPDATE {_key:@key,@fieldName:@fieldValue} IN posts")
    void updateFieldInPost(@Param("key") String key, @Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);

}
