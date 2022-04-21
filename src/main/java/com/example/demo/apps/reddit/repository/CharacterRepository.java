package com.example.demo.apps.reddit.repository;

import com.example.demo.apps.reddit.entity.Character;
import com.arangodb.springframework.repository.ArangoRepository;

import java.util.Collection;

public interface CharacterRepository extends ArangoRepository<Character, String> {
    Iterable<Character> findBySurname(String surname);
    Collection<Character> findTop2DistinctBySurnameIgnoreCaseOrderByAgeDesc(String surname);

}
