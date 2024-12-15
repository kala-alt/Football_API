package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.TeamEntity;

@Repository
public interface TeamRepository extends MongoRepository<TeamEntity, String> {

    @Query("{ '_id': ?0 }")
    List<TeamEntity> findTeam(String id);

    @Query("{ 'league_id': ?0 }")
    List<TeamEntity> findTeamsViaLeagueId(String leagueId);

    @Query("{ 'name' : { $regex: ?0, $options: 'i' } }")
    List<TeamEntity> findTeamViaName(String teamName);

    @Query("{}")
    List<TeamEntity> getAllTeams();

    @Query(value = "{'name': ?0}", fields = "{'logo_img': 1, '_id': 0}")
    byte[] getTeamLogo(String teamName);

    @Query(value = "{'name': ?0}", fields = "{'league_name': 1, '_id': 0}")
    String getLeague(String teamName);

}