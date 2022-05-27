package com.ScalableTeam.user.repositories;

import com.ScalableTeam.user.entity.User;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface UserRepository extends ArangoRepository<User, String> {
    @Query("FOR u IN users UPDATE {_key:@key,followedUsers:@followedUsers} IN users")
    void updateFollowedUsersWithID(@Param("key") String key, @Param("followedUsers") HashMap<String,Boolean> followedUsers);

    @Query("FOR u IN users UPDATE {_key:@key,blockedUsers:@blockedUsers} IN users")
    void updateBlockedUsersWithID(@Param("key") String key, @Param("blockedUsers") HashMap<String,Boolean> blockedUsers);

    @Query("FOR u IN users UPDATE {_key:@key,reportedUsers:@reportedUsers} IN users")
    void updateReportedUsersWithID(@Param("key") String key, @Param("reportedUsers") HashMap<String,String> reportedUsers);

}
