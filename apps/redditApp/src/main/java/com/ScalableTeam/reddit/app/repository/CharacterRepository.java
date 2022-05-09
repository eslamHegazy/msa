package com.ScalableTeam.reddit.app.repository;

import com.ScalableTeam.reddit.app.entity.Character;
import com.arangodb.springframework.repository.ArangoRepository;

import java.util.Collection;

public interface CharacterRepository extends ArangoRepository<Character, String> {
    Iterable<Character> findBySurname(String surname);
    Collection<Character> findTop2DistinctBySurnameIgnoreCaseOrderByAgeDesc(String surname);

}
