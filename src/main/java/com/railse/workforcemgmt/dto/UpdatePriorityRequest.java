package com.railse.workforcemgmt.dto;

import com.railse.workforcemgmt.model.Priority;

import lombok.Data;

@Data
public class UpdatePriorityRequest {
    private Priority priority;
}
