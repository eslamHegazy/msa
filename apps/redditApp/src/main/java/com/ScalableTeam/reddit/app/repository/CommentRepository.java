package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.arango.Comment;
import com.arangodb.springframework.repository.ArangoRepository;

public interface CommentRepository extends ArangoRepository<Comment, String> {
}