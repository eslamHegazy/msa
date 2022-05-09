package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.reddit.app.entity.User;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;


public interface UserRepository extends ArangoRepository<User, String> {
    @Query("FOR u IN users UPDATE {_key:@key,followedChannels:@followedChannels} IN users")
    void updateFollowedChannelsWithID(@Param("key") String key, @Param("followedChannels") HashMap<String,Boolean> followedChannels);
    @Query("FOR u IN users UPDATE {_key:@key,followedUsers:@followedUsers} IN users")
    void updateFollowedUsersWithID(@Param("key") String key, @Param("followedUsers") HashMap<String,Boolean> followedUsers);
    @Query("FOR u IN users UPDATE u WITH {followedChannels:@followedChannels} IN users OPTIONS { mergeObjects: false }")
    void updateAllUsersFollowedChannelsWithID( @Param("followedChannels") HashMap<String,Boolean> followedChannels);
    @Query("FOR u IN users UPDATE u WITH  {followedUsers:@followedUsers} IN users OPTIONS { mergeObjects: false }")
    void updateAllUsersFollowedUsersWithID( @Param("followedUsers") HashMap<String,Boolean> followedUsers);
}
