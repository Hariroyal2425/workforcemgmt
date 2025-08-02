package com.railse.workforcemgmt.mapper;

import com.railse.workforcemgmt.dto.TaskDto;
import com.railse.workforcemgmt.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto taskToTaskDto(Task task);
    Task taskDtoToTask(TaskDto taskDto);
}