package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.arango.Channel;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface ChannelRepository extends ArangoRepository<Channel, String> {
    @Query("FOR u IN channels UPDATE {_key:@key,moderators:@channels} IN channels")
    void updateModeratorsWithID(@Param("key") String key, @Param("channels") HashMap<String,Boolean> moderators);

    @Query("FOR u IN channels UPDATE {_key:@key,bannedUsers:@channels} IN channels")
    void updateBannedUsersWithID(@Param("key") String key, @Param("channels") HashMap<String, Boolean> bannedUsers);

    @Query("FOR u IN channels UPDATE {_key:@key,reports:@report} IN channels")
    void addReport(@Param("key") String key, @Param("report") HashMap<String,Boolean> report);
}
