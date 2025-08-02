package com.railse.workforcemgmt.service;

import com.railse.workforcemgmt.dto.AddCommentRequest;
import com.railse.workforcemgmt.dto.CreateTaskRequest;
import com.railse.workforcemgmt.dto.TaskDto;
import com.railse.workforcemgmt.dto.UpdatePriorityRequest;
import com.railse.workforcemgmt.mapper.TaskMapper;
import com.railse.workforcemgmt.model.Priority;
import com.railse.workforcemgmt.model.Staff;
import com.railse.workforcemgmt.model.Task;
import com.railse.workforcemgmt.model.TaskActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskMapper taskMapper;
    private final Map<String, Task> taskStore = new HashMap<>();
    private final Map<String, Staff> staffStore = new HashMap<>();

    public TaskDto createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setTitle(request.getTitle());
        task.setStatus("ACTIVE");
        task.setStartDate(request.getStartDate());
        task.setDueDate(request.getDueDate());
        task.setPriority(request.getPriority());

        Staff staff = staffStore.computeIfAbsent(request.getStaffId(), id -> {
            Staff newStaff = new Staff();
            newStaff.setId(id);
            newStaff.setName("Staff " + id);
            return newStaff;
        });
        task.setAssignedStaff(staff);

        TaskActivity activity = new TaskActivity();
        activity.setDescription("Task created by system");
        activity.setTimestamp(LocalDateTime.now());
        task.getActivities().add(activity);

        taskStore.put(task.getId(), task);
        return taskMapper.taskToTaskDto(task);
    }

    public TaskDto assignTaskByRef(String taskId, String newStaffId) {
        Task existingTask = taskStore.get(taskId);
        if (existingTask == null || "CANCELLED".equals(existingTask.getStatus())) {
            throw new RuntimeException("Task not found or already cancelled");
        }
        
        existingTask.setStatus("CANCELLED");
        TaskActivity cancelActivity = new TaskActivity();
        cancelActivity.setDescription("task Canceled for re assigned by system");
        cancelActivity.setTimestamp(LocalDateTime.now());
        existingTask.getActivities().add(cancelActivity);
        
        // Create new task for new staff
        Task newTask = new Task();
        newTask.setId(UUID.randomUUID().toString());
        newTask.setTitle(existingTask.getTitle());
        newTask.setStatus("ACTIVE");
        newTask.setStartDate(existingTask.getStartDate());
        newTask.setDueDate(existingTask.getDueDate());
        newTask.setPriority(existingTask.getPriority());

        Staff newStaff = staffStore.computeIfAbsent(newStaffId, id -> {
            Staff staff = new Staff();
            staff.setId(id);
            staff.setName("Staff " + id);
            return staff;
        });
        newTask.setAssignedStaff(newStaff);

        TaskActivity assignActivity = new TaskActivity();
        assignActivity.setDescription("Task reassigned to " + newStaff.getName() + " by system");
        assignActivity.setTimestamp(LocalDateTime.now());
        newTask.getActivities().add(assignActivity);

        taskStore.put(newTask.getId(), newTask);
        return taskMapper.taskToTaskDto(newTask);
    }

    public List<TaskDto> getTasksByDateRange(String staffId, LocalDateTime start, LocalDateTime end) {
        return taskStore.values().stream()
        		.filter(task -> !task.getStatus().equals("CANCELLED"))
                .filter(task -> task.getAssignedStaff().getId().equals(staffId))
                .filter(task->{
                	LocalDateTime taskStart =task.getStartDate();
                	return (taskStart.isEqual(start) || taskStart.isAfter(start)) && (taskStart.isEqual(end)|| (taskStart.isBefore(end)));
                })
                .map(taskMapper::taskToTaskDto)
                .collect(Collectors.toList());
    }


    public TaskDto updateTaskPriority(String taskId, UpdatePriorityRequest request) {
        Task task = taskStore.get(taskId);
        if (task == null || "CANCELLED".equals(task.getStatus())) {
            throw new RuntimeException("Task not found or already cancelled");
        }

        task.setPriority(request.getPriority());
        TaskActivity activity = new TaskActivity();
        activity.setDescription("Priority changed to " + request.getPriority() + " by system");
        activity.setTimestamp(LocalDateTime.now());
        task.getActivities().add(activity);

        return taskMapper.taskToTaskDto(task);
    }

    public List<TaskDto> getTasksByPriority(Priority priority) {
        return taskStore.values().stream()
                .filter(task -> !task.getStatus().equals("CANCELLED"))
                .filter(task -> task.getPriority() == priority)
                .map(taskMapper::taskToTaskDto)
                .collect(Collectors.toList());
    }

    public TaskDto addComment(String taskId, AddCommentRequest request) {
        Task task = taskStore.get(taskId);
        if (task == null || "CANCELLED".equals(task.getStatus())) {
            throw new RuntimeException("Task not found or already cancelled");
        }

        TaskActivity activity = new TaskActivity();
        activity.setDescription("Comment added: " + request.getComment() + " by system");
        activity.setTimestamp(LocalDateTime.now());
        task.getActivities().add(activity);

        return taskMapper.taskToTaskDto(task);
    }

    public TaskDto getTaskById(String taskId) {
        Task task = taskStore.get(taskId);
        if (task == null || "CANCELLED".equals(task.getStatus())) {
            throw new RuntimeException("Task not found or cancelled");
        }
        return taskMapper.taskToTaskDto(task);
    }
}