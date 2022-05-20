package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.reddit.app.entity.Comment;
import com.arangodb.springframework.repository.ArangoRepository;

public interface CommentRepository extends ArangoRepository<Comment, String> {
}