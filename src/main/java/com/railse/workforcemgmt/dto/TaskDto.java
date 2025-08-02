package com.railse.workforcemgmt.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.railse.workforcemgmt.model.Priority;
import com.railse.workforcemgmt.model.Staff;
import com.railse.workforcemgmt.model.TaskActivity;

import lombok.Data;

@Data
public class TaskDto {
    private String id;
    private String title;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private Staff assignedStaff;
    private Priority priority;
    private List<TaskActivity> activities;
}

