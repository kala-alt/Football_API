package com.example.demo.Repositories;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import com.example.demo.Entities.AccountEntity;

@Repository
public interface AccountRepository extends MongoRepository<AccountEntity, String>{

    @Query(value = "{}", count = true)
    int NumOfUsers();

    @Query(value = "{'username': ?0 ,'password' : ?1 }", exists = true)    
    boolean doesAccountExist(String username, String password);

    @Query("{'_id': ?0}")    
    AccountEntity findUserById(String accountId);

    @Query("{'username': ?0}")    
    AccountEntity findUserByUsername(String username);

    @Query("{'api_key': ?0}")    
    AccountEntity findUserByApiKey(String api_key);

    @Query("{'email': ?0}")    
    AccountEntity findUserByEmail(String userEmail);

    @Query(value = "{'email': ?0}", exists = true)    
    Boolean ExistsUserByEmail(String userEmail);

    // @Query("{'username' : ?0, 'password' : ?1}")
    // AccountEntity findLoginUser(String username, String password);

    @Query("{'isAdmin' : true}")
    ArrayList<AccountEntity> findAllAdmins();

    @Query("{'_id' : ?0, 'isAdmin' : true}")
    Boolean checkIsAdmin(String id);

    @Query("{}")
    ArrayList<AccountEntity> findAllAccounts();

}
