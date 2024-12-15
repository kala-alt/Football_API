package com.example.demo.Entities;

public class EventEntity {

    private String playerId, player, team, type;
    private int minute;

    public EventEntity(){
        
    }

    public EventEntity(String player, String type, int minute, String playerId, String team) {
        this.playerId = playerId;
        this.player = player;
        this.team = team;
        this.type = type;
        this.minute = minute;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
