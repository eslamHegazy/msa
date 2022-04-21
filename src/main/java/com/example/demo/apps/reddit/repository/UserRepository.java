package com.example.demo.apps.reddit.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import com.example.demo.apps.reddit.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;


public interface UserRepository extends ArangoRepository<User, String> {
    @Query("FOR u IN users UPDATE {_key:@key,followedChannels:@channels} IN users")
    void updateFollowedChannelsWithID(@Param("key") String key, @Param("channels")HashSet<String>channels);

}
