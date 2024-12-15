package com.example.demo.Entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Teams")
public class TeamEntity {
    
    @Id
    private String id;
    private String name, city, league_code, league_id, league_name, stadium;
    private int concededGoals, playedMatches, NumRedCards, NumYellowCards, scoredGoals, standing, wins, draws, losses, points;
    private byte[] logo_img;

    public int getScoredGoals() {
        return scoredGoals;
    }
    public void setScoredGoals(int scoredGoals) {
        this.scoredGoals = scoredGoals;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }
    public int getDraws() {
        return draws;
    }
    public void setDraws(int draws) {
        this.draws = draws;
    }
    public int getLosses() {
        return losses;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }
    public String getStadium() {
        return stadium;
    }
    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getLeague_code() {
        return league_code;
    }
    public void setLeague_code(String league_code) {
        this.league_code = league_code;
    }
    public String getLeague_id() {
        return league_id;
    }
    public void setLeague_id(String league_id) {
        this.league_id = league_id;
    }
    public String getLeague_name() {
        return league_name;
    }
    public void setLeague_name(String league_name) {
        this.league_name = league_name;
    }
    public int getConcededGoals() {
        return concededGoals;
    }
    public void setConcededGoals(int concededGoals) {
        this.concededGoals = concededGoals;
    }
    public int getPlayedMatches() {
        return playedMatches;
    }
    public void setPlayedMatches(int playedMatches) {
        this.playedMatches = playedMatches;
    }
    public int getNumRedCards() {
        return NumRedCards;
    }
    public void setNumRedCards(int NumRedCards) {
        this.NumRedCards = NumRedCards;
    }
    public int getNumYellowCards() {
        return NumYellowCards;
    }
    public void setNumYellowCards(int NumYellowCards) {
        this.NumYellowCards = NumYellowCards;
    }

    public int getStanding() {
        return standing;
    }

    public void setStanding(int standing) {
        this.standing = standing;
    }

    public byte[] getLogo_img() {
        return logo_img;
    }
    
    public void setLogo_img(byte[] logo_img) {
        this.logo_img = logo_img;
    }
}
