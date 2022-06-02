package com.ScalableTeam.arango;

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
    @Query("FOR u IN users UPDATE {_key:@key,blockedUsers:@blockedUsers} IN users")
    void updateBlockedUsersWithID(@Param("key") String key, @Param("blockedUsers") HashMap<String,Boolean> blockedUsers);
    @Query("FOR u IN users UPDATE {_key:@key,reportedUsers:@reportedUsers} IN users")
    void updateReportedUsersWithID(@Param("key") String key, @Param("reportedUsers") HashMap<String,String> reportedUsers);

    @Query("FOR u IN users UPDATE {_key:@key,bookmarkedPosts:@bookmarkedPosts} IN users")
    void updateBookmarkedPosts(@Param("key") String key, @Param("bookmarkedPosts") HashMap<String,Boolean> bookmarkedPosts);

    @Query("FOR u IN users UPDATE {_key:@key,bookmarkedChannels:@bookmarkedChannels} IN users")
    void updateBookmarkedChannels(@Param("key") String key, @Param("bookmarkedChannels") HashMap<String,Boolean> bookmarkedChannels);

    void deleteByUserNameId(String userNameId);

    boolean existsByUserNameId(String userNameId);
//    @Query("FOR u IN users REMOVE {_key:@key,followedChannels:@followedChannels} IN users")
//    void removeFollowedChannelsWithID(@Param("key") String key, @Param("followedChannels") HashMap<String,Boolean> followedChannels);
    @Query("FOR u IN users REMOVE {_key:@key,followedChannels:@followedChannels} IN users")
    void removeFollowedChannelsWithID(@Param("key") String key, @Param("followedChannels") HashMap<String,Boolean> followedChannels);


}
