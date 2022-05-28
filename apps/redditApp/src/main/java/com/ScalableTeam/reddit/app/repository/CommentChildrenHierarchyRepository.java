package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.arango.CommentToComment;
import com.arangodb.springframework.repository.ArangoRepository;

public interface CommentChildrenHierarchyRepository extends ArangoRepository<CommentToComment, String> {
}

