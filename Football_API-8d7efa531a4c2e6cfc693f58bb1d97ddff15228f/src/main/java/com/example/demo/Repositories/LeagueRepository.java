package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.LeagueEntity;

@Repository
public interface LeagueRepository extends MongoRepository<LeagueEntity, String> {

    @Query("{}")
    List<LeagueEntity> getAllLeagues();
    
    @Query("{ $or: [ { 'name' : { $regex: ?0, $options: 'i' } }, { 'code' : { $regex: ?0, $options: 'i' } }, { 'country' : { $regex: ?0, $options: 'i' } } ] }")
    List<LeagueEntity> findLeague(String searchLeague);

    @Query(value = "{}", count = true)
    int numberOfLeagueTeams();
}