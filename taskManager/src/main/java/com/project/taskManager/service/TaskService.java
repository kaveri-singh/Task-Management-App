package com.project.taskManager.service;

import com.project.taskManager.entity.Tasks;
import com.project.taskManager.entity.Users;
import com.project.taskManager.repository.TaskRepository;
import com.project.taskManager.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveTask(Tasks task, String username){
        try{
            Users user = userService.findByUsername(username);
            task.setCreatedDate(LocalDateTime.now());
            Tasks saved = taskRepository.save(task);
            user.getUserTasks().add(saved);
            userService.saveUser(user);
        } catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occurred while saving the entry.", e);
        }
    }

    public void saveTask(Tasks task){
        taskRepository.save(task);
    }

    public List<Tasks> getAll(){
        return taskRepository.findAll();
    }

    public Optional<Tasks> getById(ObjectId id){
        return taskRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username){
        boolean removed = false;
        Users user = userService.findByUsername(username);
        removed = user.getUserTasks().removeIf(x -> x.getId().equals(id));
        if(removed){
            userService.saveUser(user);
            taskRepository.deleteById(id);
        }
        return removed;
    }

}
