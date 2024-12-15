package com.example.demo.Entities;


import java.io.IOException;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Document(collection = "Leagues")
public class LeagueEntity {

    @Id
    private String id;

    private String name, code, country;

    private byte[] logoImg, countryFlag;

    private int numberOfTeams;


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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(int numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public byte[] getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(byte[] logoImg) throws IOException {
        this.logoImg = logoImg;
    }

    public byte[] getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(byte[] countryFlag) throws IOException {
        this.countryFlag = countryFlag;
    }
}