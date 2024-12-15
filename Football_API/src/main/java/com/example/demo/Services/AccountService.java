package com.example.demo.Services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.AccountEntity;
import com.example.demo.Repositories.AccountRepository;

@Configuration
@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public void saveAccount(AccountEntity accountEntity) {
        if(findUserByUsername(accountEntity.getUsername()) == null)
            accountEntity.setPassword(passwordEncoder().encode(accountEntity.getPassword()));
            
        accountRepository.save(accountEntity);
    }


    public AccountEntity findUserById(String accountId){
        return accountRepository.findUserById(accountId);
    } 

    public AccountEntity findUserByApiKey(String api_key){
        return accountRepository.findUserByApiKey(api_key);
    }

    public boolean doesAccountExist(String username, String password){
        return accountRepository.doesAccountExist(username, password);
    } 

    public AccountEntity findUserByUsername(String username){
        return accountRepository.findUserByUsername(username);
    }

    public AccountEntity findLoginUser(String username, String password){
        AccountEntity accountEntity = findUserByUsername(username);

        try{
            System.out.println("We get an account: " + accountEntity.getUsername() + 
        " pass match: " + passwordEncoder().matches(password, accountEntity.getPassword()) + 
        "\npassword: " + accountEntity.getPassword() + 
        " raw password: " + password );

            if (passwordEncoder().matches(password, accountEntity.getPassword())) 
                return accountEntity;
            else
                return null;
        }catch(Exception e){
            return null;
        }

        }

    public AccountEntity findUserByEmail(String userEmail){
        return accountRepository.findUserByEmail(userEmail);
    }

    public ArrayList<AccountEntity> findAllAdmins(){
        return accountRepository.findAllAdmins();
    }

    public ArrayList<AccountEntity> findAllAccounts(){
        return accountRepository.findAllAccounts();
    }

    public boolean checkIsAdmin(String id){
        return accountRepository.checkIsAdmin(id);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void setNewLastLogin(String id) throws Exception{
            AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(
            ()-> new Exception("We have a problem with setNewLastLogin()"));

            accountEntity.setLast_login(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

            accountRepository.save(accountEntity);
    }

    public void nullMonthQueries(String id) throws Exception{
        AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(
        ()-> new Exception("We have a problem with nullMonthQueries()"));
        accountEntity.setQueriesThisMonth(0);
        accountRepository.save(accountEntity);
}


    
}
