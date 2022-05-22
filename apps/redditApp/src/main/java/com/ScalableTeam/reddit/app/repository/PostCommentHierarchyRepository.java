package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.reddit.app.entity.PostToComment;
import com.arangodb.springframework.repository.ArangoRepository;

public interface PostCommentHierarchyRepository extends ArangoRepository<PostToComment, String> {
}
