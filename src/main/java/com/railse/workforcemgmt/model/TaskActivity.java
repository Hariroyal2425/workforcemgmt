package com.railse.workforcemgmt.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskActivity {
    private String description;
    private LocalDateTime timestamp;
}
