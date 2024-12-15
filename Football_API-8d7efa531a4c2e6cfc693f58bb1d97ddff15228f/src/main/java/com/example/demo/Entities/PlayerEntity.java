package com.example.demo.Entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "Players")
public class PlayerEntity {
    
    @Id
    private String playerId;
    private String first_name, last_name, position, nationality, team_id, team;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_Of_Birth, join;

    private byte[] picture;
    private int numbeOfGoals, yellowCards, redCards;
    private boolean retired;

    public PlayerEntity(){
        this.retired = false;
    }

    public int getNumbeOfGoals() {
        return numbeOfGoals;
    }
    public void setNumbeOfGoals(int numbeOfGoals) {
        this.numbeOfGoals = numbeOfGoals;
    }
    public boolean isRetired() {
        return retired;
    }
    public void setRetired(boolean retired) {
        this.retired = retired;
    }
    public String getPlayerId() {
        return playerId;
    }
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public String getTeam_id() {
        return team_id;
    }
    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }
    public Date getDate_Of_Birth() {
        return date_Of_Birth;
    }
    public void setDate_Of_Birth(Date date_Of_Birth) {
        this.date_Of_Birth = date_Of_Birth;
    }
    public Date getJoin() {
        return join;
    }
    public void setJoin(Date join) {
        this.join = join;
    }
    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture=picture;
    }
    
    public int getYellowCards() {
        return yellowCards;
    }
    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }
    public int getRedCards() {
        return redCards;
    }
    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }
    public String getTeam() {
        return team;
    }
    public void setTeam(String team) {
        this.team = team;
    }
}
