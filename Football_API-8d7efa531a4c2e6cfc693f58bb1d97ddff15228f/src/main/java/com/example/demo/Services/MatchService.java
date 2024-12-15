package com.example.demo.Services;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.MatchEntity;
import com.example.demo.Repositories.MatchRepository;

@Service
public class MatchService{

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamService teamService;

    public List<MatchEntity> getAllMatches() {
        return matchRepository.findAllMatches();
    }

    public List<MatchEntity> getAllMatchesViaDate(String date) {
        List<MatchEntity> list =  matchRepository.findAllMatchesViaDate(date);
        Collections.sort(list, (m1, m2) -> {
        
            int leagueComparison =  ((MatchEntity) m1).getLeague().compareTo(((MatchEntity) m2).getLeague());
            
            if (leagueComparison == 0) {
                return ((MatchEntity) m1).getTime().compareTo(((MatchEntity) m2).getTime());
            }
        
            return leagueComparison;
        });
        return list; 
    }

    public void saveMatch(MatchEntity match) {
        matchRepository.save(match);
    }

    public MatchEntity findMatchById(String id) {
        return matchRepository.findById(id).orElse(null);
    }

    public void deleteMatchById(String id) {
        matchRepository.deleteById(id);
    }

    public Boolean checkAlreadyAddedMatch(String home_teamId, String away_teamId, String leagueId, int round){
        return matchRepository.checkAlreadyAddedMatch(home_teamId, away_teamId, leagueId, round);
    }

    //TODO We have a problem with images here
    public String getTeamLogoAsBase64(byte[] img) throws IOException{
        return  "data:image/png;base64," + Base64.getEncoder().encodeToString(img);
    }

    public String getTeamLeagueMS(String teamName) {
        return teamService.getLeague(teamName);
    }
}
