package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.reddit.app.entity.CommentToComment;
import com.arangodb.springframework.repository.ArangoRepository;

public interface CommentChildrenHierarchyRepository extends ArangoRepository<CommentToComment, String> {
}

