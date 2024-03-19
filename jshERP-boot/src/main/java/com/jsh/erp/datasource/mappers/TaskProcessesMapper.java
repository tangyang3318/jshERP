package com.jsh.erp.datasource.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.datasource.entities.TaskMaterial;
import com.jsh.erp.datasource.entities.TaskProcesses;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface TaskProcessesMapper extends BaseMapper<TaskProcesses> {
    Page<TaskProcesses> searchTaskProcesses(IPage<TaskProcesses> page, @Param("taskProcesses") TaskProcesses taskProcesses);


    List<TaskProcesses> searchTaskProcesses(@Param("taskProcesses") TaskProcesses taskProcesses);

    Map<String, Object> getTaskPostponeList();
}
