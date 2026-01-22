package com.project.taskManager.controller;

import com.project.taskManager.entity.Tasks;
import com.project.taskManager.entity.Users;
import com.project.taskManager.service.TaskService;
import com.project.taskManager.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {


    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllTasksOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.findByUsername(username);
        List<Tasks> t = user.getUserTasks();

        if (t == null || t.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        t.sort(Comparator
                .comparing(Tasks::getPriority)
                .thenComparing(task -> {
                    String status = String.valueOf(task.getStatus());
                    return status != null && status.equalsIgnoreCase("DONE") ? 1 : 0;
                }));
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tasks> createEntry(@RequestBody Tasks myTask) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            taskService.saveTask(myTask, username);
            return new ResponseEntity<>(myTask, HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{myId}")
    public ResponseEntity<Tasks> getTaskEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.findByUsername(username);
        List<Tasks> collect = user.getUserTasks().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<Tasks> task = taskService.getById(myId);
            if(task.isPresent()){
                return new ResponseEntity<>(task.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{myId}")
    public ResponseEntity<?> deleteTaskEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = taskService.deleteById(myId, username);
        if(removed){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId id, @RequestBody Tasks newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Users user = userService.findByUsername(userName);
        List<Tasks> collect = user.getUserTasks().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<Tasks> task = taskService.getById(id);
            if(task.isPresent()){
                Tasks old = task.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setDescription(newEntry.getDescription() != null && !newEntry.getDescription().equals("") ? newEntry.getDescription() : old.getDescription());
                old.setPriority(newEntry.getPriority() != null && !newEntry.getPriority().equals("") ? newEntry.getPriority() : old.getPriority());
                old.setStatus(newEntry.getStatus() != null && !newEntry.getStatus().equals("") ? newEntry.getStatus() : old.getStatus());
                old.setDueDate(newEntry.getDueDate() != null && !newEntry.getDueDate().equals("") ? newEntry.getDueDate() : old.getDueDate());
                taskService.saveTask(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}


