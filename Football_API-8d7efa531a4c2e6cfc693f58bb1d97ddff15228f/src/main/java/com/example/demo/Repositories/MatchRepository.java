package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.MatchEntity;

@Repository
public interface MatchRepository extends MongoRepository<MatchEntity, String> {

    @Query("{}")
    List<MatchEntity> findAllMatches();

    @Query("{'date' : ?0}")
    List<MatchEntity> findAllMatchesViaDate(String date);

    @Query("{'home_teamId' : ?0, 'away_teamId' : ?1, 'leagueId' : ?2, 'round' : ?3}")
    Boolean checkAlreadyAddedMatch(String home_teamId, String away_teamId, String leagueId, int round);

}