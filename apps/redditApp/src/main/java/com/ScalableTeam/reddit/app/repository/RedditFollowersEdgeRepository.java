package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.arango.RedditFollowersEdge;
import com.ScalableTeam.arango.User;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;

public interface RedditFollowersEdgeRepository extends ArangoRepository<RedditFollowersEdge, String> {

//    FOR s IN ${subtitlesEdges}
//    FILTER s._to == ${_id}
//    REMOVE s IN ${subtitlesEdges}
@Query("FOR f IN redditFollowers FILTER f._from == @channel AND f._to==@user  REMOVE f IN redditFollowers")
void unfollow(@Param("channel") String channelId, @Param("user") String userId);
@Query("FOR f IN redditFollowers FILTER f._from._key == @channel RETURN f._to")
Iterable<User> getFollowers(@Param("channel") String redditId);

    @Query("FOR c IN channels FILTER c._key==@channel FOR f IN 1..1 OUTBOUND c redditFollowers REMOVE f")
    Iterable<User> getChannelFollowers(@Param("channel") String redditId);


//    @Query("FOR p IN posts FILTER CONTAINS(LOWER(p.title), LOWER(@title)) RETURN p")
//    Post[] getPostsByPostTitle(@Param("title") String title);

}
