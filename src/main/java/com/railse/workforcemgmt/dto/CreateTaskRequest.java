package com.railse.workforcemgmt.dto;

import java.time.LocalDateTime;

import com.railse.workforcemgmt.model.Priority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTaskRequest {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String staffId;
    private Priority priority;
}