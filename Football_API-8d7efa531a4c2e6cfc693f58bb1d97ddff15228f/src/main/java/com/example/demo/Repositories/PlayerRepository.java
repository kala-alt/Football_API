package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.PlayerEntity;

@Repository
public interface PlayerRepository extends MongoRepository<PlayerEntity, String> {

    @Query("{'retired' : false}")
    List<PlayerEntity> findAllActivePlayers();

    @Query("{ $or: [ { 'first_name' : { $regex: ?0, $options: 'i' } }, { 'last_name' : { $regex: ?0, $options: 'i' } } ] }")
    List<PlayerEntity> findActivePlayerViaName(String searchTerm);


    @Query("{ 'team': { $in: ['?0', '?1'] } }")
    List<PlayerEntity> getMatchPlayers(String homeTeam, String awayTeam);


    @Query(value = "{'_id' : ?0}")
    PlayerEntity findPlayerViaId(String playerId);

}