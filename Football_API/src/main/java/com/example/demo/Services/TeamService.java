package com.example.demo.Services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.TeamEntity;
import com.example.demo.Repositories.TeamRepository;


@Service
public class TeamService{

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MongoOperations mongoOperations;

    public List<TeamEntity> getAllTeams() {
        return teamRepository.getAllTeams();
    }

    public List<TeamEntity> findTeamsViaLeagueId(String leagueId){
        return teamRepository.findTeamsViaLeagueId(leagueId);
    }


    public void saveTeam(TeamEntity team) {
        teamRepository.save(team);
    }

    public TeamEntity findTeamById(String id) {
        return teamRepository.findById(id).orElse(null);
    }

    //Works perfectly
    public byte[] getTeamLogo(String teamName){
        System.out.println("\n\nTeam service image: " + teamRepository.getTeamLogo(teamName));
        return teamRepository.getTeamLogo(teamName);
    }

    public String getLeague(String teamName){
        return teamRepository.getLeague(teamName);
    }

    public List<TeamEntity> findTeamViaName(String teamName){
        return teamRepository.findTeamViaName(teamName);
    }

    
    //Works
    public void updatePointsAndStanding(String competitionId){

        for(TeamEntity team : teamRepository.findTeamsViaLeagueId(competitionId)){
            mongoOperations.updateFirst(
                new Query(Criteria.where("_id").is(team.getId())), 
                new Update()
                    .set("points", team.getWins()*3 + team.getDraws()),
                TeamEntity.class);
        }

        List<TeamEntity> teams = teamRepository.findTeamsViaLeagueId(competitionId);

        Collections.sort(teams, (t1, t2) -> Integer.compare(t2.getPoints(), t1.getPoints()));

        int position = 1;

        // TODO Test if this sorting algorithm is working properly
        for(int i=0; i<teams.size(); i++){
            if (i<teams.size()-1)
                if (teams.get(i).getPoints() == teams.get(i+1).getPoints()){

                    int goalDiff1 = teams.get(i).getScoredGoals() - teams.get(i).getConcededGoals();
                    int goalDiff2 = teams.get(i+1).getScoredGoals() - teams.get(i+1).getConcededGoals();

                    if (goalDiff1 < goalDiff2 || (goalDiff1 == goalDiff2 && teams.get(i).getScoredGoals() < teams.get(i+1).getScoredGoals()))
                        Collections.swap(teams, i, i+1);
                }
            
            mongoOperations.updateFirst(
                new Query(Criteria.where("_id").is(teams.get(i).getId())), 
                new Update()
                    .set("standing", position++),
                TeamEntity.class);
        }
    }
}
