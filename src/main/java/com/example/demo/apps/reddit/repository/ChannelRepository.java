package com.example.demo.apps.reddit.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import com.example.demo.apps.reddit.entity.Channel;
import com.example.demo.apps.reddit.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;

public interface ChannelRepository extends ArangoRepository<Channel, String> {
    @Query("FOR u IN channels UPDATE {_key:@key,moderators:@channels} IN channels")
    void updateModeratorsWithID(@Param("key") String key, @Param("channels") HashSet<User> moderators);
}
