package com.project.taskManager.repository;

import com.project.taskManager.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<Users, ObjectId> {
    Users findByUsername(String username);
    void deleteByUsername(String username);
}
