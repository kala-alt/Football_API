package com.example.demo.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.MatchEntity;

@Repository
public interface EventRepository extends MongoRepository<MatchEntity, String> {

    
    
}
