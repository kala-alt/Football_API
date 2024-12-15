package com.example.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.LeagueEntity;
import com.example.demo.Repositories.LeagueRepository;

@Service
public class LeagueService {
    
    @Autowired
    private LeagueRepository leagueRepository;

    public List<LeagueEntity> getAllLeagues(){
        return leagueRepository.getAllLeagues();
    }

    public void saveLeague(LeagueEntity league) {
        leagueRepository.save(league);
    }
    
    public LeagueEntity findLeagueById(String id) {
        return leagueRepository.findById(id).orElse(null);
    }

    public void deleteLeagueById(String id) {
        leagueRepository.deleteById(id);
    }

    public List<LeagueEntity> findLeague(String searchLeague){
        return leagueRepository.findLeague(searchLeague);
    }


    public int numberOfLeagueTeams(){
        return leagueRepository.numberOfLeagueTeams();
    }

}
