package com.example.demo.Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Document(collection = "Users")
public class AccountEntity {

    public AccountEntity(){
        this.isAdmin = false;
        this.api_key = "Comming Soon";
        this.last_login = null;
        this.paid_to = Date.from(LocalDate.now().plusDays(-1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.last_login = Date.from(LocalDate.now().plusDays(-1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.reg_date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.queriesThisMonth = 0;
    }

    @NotBlank(message = "Enter your username!")
    @Size(min=2, max=5, message = "Username must be between 2 and 5 characters")
    private String username;


    @NotBlank(message = "Enter your password!")
    @Size(min=2, max=5, message = "Password must be between 2 and 5 characters")
    private String password;

    @NotBlank(message = "Enter your email!")
    private String email;

    @Id
    private String id;
    
    private String api_key;
    private Boolean isAdmin;
    private Date last_login, paid_to, reg_date;
    private int queriesThisMonth;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQueriesThisMonth() {
        return queriesThisMonth;
    }

    public void setQueriesThisMonth(int queriesThisMonth) {
        this.queriesThisMonth = queriesThisMonth;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Date getLast_login(){
        return last_login;
    }

    public String getStringLast_login() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime localDateTime = last_login.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.format(formatter);
    }

    public void setLast_login(Date last_login) {
        System.out.println("New last login date: " + last_login);
        this.last_login = last_login;
    }

    public Date getPaid_to() {
        return paid_to;
    }

    public void setPaid_to(Date paid_to) {
        this.paid_to = paid_to;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public String getStringReg_date() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime localDateTime = reg_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();    
        return localDateTime.format(formatter);
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        System.out.println("New set password: " + this.password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}