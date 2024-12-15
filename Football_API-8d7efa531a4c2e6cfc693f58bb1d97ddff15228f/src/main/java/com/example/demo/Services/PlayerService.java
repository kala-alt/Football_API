package com.example.demo.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.PlayerEntity;
import com.example.demo.Repositories.PlayerRepository;

@Service
public class PlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    public List<PlayerEntity> getAllPlayers(){
        return playerRepository.findAll();
    }

    public void savePlayer(PlayerEntity player) {
        System.out.println("\n\nPlayerService is running...\n\n");
        playerRepository.save(player);
    }

    public ArrayList<PlayerEntity> getMatchPlayers(String homeTeam, String awayTeam){
        ArrayList<PlayerEntity> list = new ArrayList<PlayerEntity>(playerRepository.getMatchPlayers(homeTeam, awayTeam));

        for(PlayerEntity player : list)
            if (player.isRetired())
                list.remove(player);
            
        return list;
    }


    public List<PlayerEntity> findPlayerViaName(String playerName){
        return playerRepository.findActivePlayerViaName(playerName);
    }

    public PlayerEntity findPlayerViaId(String playerId){
        return playerRepository.findPlayerViaId(playerId);
    }

    // public MatchEntity findMatchById(String id) {
    //     return matchRepository.findById(id).orElse(null);
    // }

    // public void deleteMatchById(String id) {
    //     matchRepository.deleteById(id);
    // }

    // public void findPlayer(String id){
    //     matchRepository.findPlayer(id);
    // }
    
}
