package com.project.taskManager.repository;

import java.util.List;
import java.util.Optional;
import com.project.taskManager.entity.Tasks;
import com.project.taskManager.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Tasks, ObjectId> {
}
