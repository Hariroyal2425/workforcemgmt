package com.railse.workforcemgmt.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String id;
    private String title;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private Staff assignedStaff;
    private Priority priority;
    private List<TaskActivity> activities = new ArrayList<>();
}
