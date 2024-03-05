package com.jsh.erp.datasource.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsh.erp.datasource.entities.TaskMaterial;
import com.jsh.erp.datasource.entities.TaskProcesses;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskProcessesMapper extends BaseMapper<TaskProcesses> {
}
