package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.arango.PostToComment;
import com.arangodb.springframework.repository.ArangoRepository;

public interface PostCommentHierarchyRepository extends ArangoRepository<PostToComment, String> {
}
