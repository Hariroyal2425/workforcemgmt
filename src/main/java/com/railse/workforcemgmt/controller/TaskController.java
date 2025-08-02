package com.railse.workforcemgmt.controller;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.railse.workforcemgmt.dto.AddCommentRequest;
import com.railse.workforcemgmt.dto.CreateTaskRequest;
import com.railse.workforcemgmt.dto.TaskDto;
import com.railse.workforcemgmt.dto.UpdatePriorityRequest;
import com.railse.workforcemgmt.model.Priority;
import com.railse.workforcemgmt.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/create-task")
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequest request) {
        TaskDto task = taskService.createTask(request);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}/assign")
    public ResponseEntity<TaskDto> assignTask(@PathVariable String taskId, 
                                             @RequestParam String staffId) {
        TaskDto task = taskService.assignTaskByRef(taskId, staffId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<TaskDto>> getTasksByDateRange(@PathVariable String staffId , LocalDateTime start , LocalDateTime end) {
        List<TaskDto> tasks = taskService.getTasksByDateRange(staffId, start, end);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{taskId}/priority")
    public ResponseEntity<TaskDto> updateTaskPriority(@PathVariable String taskId, 
                                                     @RequestBody UpdatePriorityRequest request) {
        TaskDto task = taskService.updateTaskPriority(taskId, request);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskDto>> getTasksByPriority(@PathVariable Priority priority) {
        List<TaskDto> tasks = taskService.getTasksByPriority(priority);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/{taskId}/comment")
    public ResponseEntity<TaskDto> addComment(@PathVariable String taskId, 
                                             @RequestBody AddCommentRequest request) {
        TaskDto task = taskService.addComment(taskId, request);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable String taskId) {
        TaskDto task = taskService.getTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}