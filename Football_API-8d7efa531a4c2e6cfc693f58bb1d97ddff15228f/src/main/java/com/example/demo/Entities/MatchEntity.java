package com.example.demo.Entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Matches")
public class MatchEntity {
    @Id
    private String id;

    private String date;

    private String time;

    private String home_team, away_team, home_teamId, away_teamId, stadium, league, leagueId;

    private int round;

    private Boolean finished;

    private List<EventEntity> goals = new ArrayList<EventEntity>();

    private List<EventEntity> yellow_cards = new ArrayList<EventEntity>();

    private List<EventEntity> red_cards = new ArrayList<EventEntity>();
    

    //Getters and Setters

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public List<EventEntity> getYellow_cards() {
        return yellow_cards;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public void setYellow_cards(List<EventEntity> yellow_cards) {
        this.yellow_cards = yellow_cards;
    }

    public List<EventEntity> getRed_cards() {
        return red_cards;
    }
    
    public void setRed_cards(List<EventEntity> red_cards) {
        this.red_cards = red_cards;
    } 

     public String getHome_teamId() {
        return home_teamId;
    }

    public void setHome_teamId(String home_teamId) {
        this.home_teamId = home_teamId;
    }

    public String getAway_teamId() {
        return away_teamId;
    }

    public void setAway_teamId(String away_teamId) {
        this.away_teamId = away_teamId;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = String.valueOf(date);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHome_team() {
        return home_team;
    }

    public void setHome_team(String home_team) {
        this.home_team = home_team;
    }

    public String getAway_team() {
        return away_team;
    }

    public void setAway_team(String away_team) {
        this.away_team = away_team;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public List<EventEntity> getGoals() {
        return goals;
    }

    public void setGoals(List<EventEntity> goals) {
        this.goals = goals;
    }

    public String home_goals(){
        System.out.println("\n\n\nhome_goals() is running....");
        int counter = 0;
        for(EventEntity g : goals){
            if(g.getTeam().equals(home_team))
                counter++;
        }
        return String.valueOf(counter);
    }

    public String away_goals(){
        System.out.println("\n\n\naway_goals() is running....");
        int counter = 0;
        for(EventEntity g : goals){
            if(g.getTeam().equals(away_team))
                counter++;
        }
        return String.valueOf(counter);
    }


    public int numOfHome_goals(){
        int counter = 0;
        for(EventEntity g : goals){
            if(g.getTeam().equals(home_team))
                counter++;
        }
        return counter;
    }


    public int numOfAway_goals(){
        int counter = 0;
        for(EventEntity g : goals){
            if(g.getTeam().equals(away_team))
                counter++;
        }
        return counter;
    }

    public int numOfHomeTeamYellow_cards(){
        int counter = 0;
        for(EventEntity g : yellow_cards){
            if(g.getTeam().equals(home_team))
                counter++;
        }
        return counter;
    }

    public int numOfAwayTeamYellow_cards(){
        int counter = 0;
        for(EventEntity g : yellow_cards){
            if(g.getTeam().equals(away_team))
                counter++;
        }
        return counter;
    }

    public int numOfHomeTeamRed_cards(){
        int counter = 0;
        for(EventEntity g : red_cards){
            if(g.getTeam().equals(home_team))
                counter++;
        }
        return counter;
    }

    public int numOfAwayTeamRed_cards(){
        int counter = 0;
        for(EventEntity g : red_cards){
            if(g.getTeam().equals(away_team))
                counter++;
        }
        return counter;
    }
}