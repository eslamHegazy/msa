package com.example.demo.apps.reddit.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.example.demo.apps.reddit.entity.Comment;

public interface CommentRepository extends ArangoRepository<Comment, String> {
}
