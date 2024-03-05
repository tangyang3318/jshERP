package com.jsh.erp.service.taskProcesses;

import com.jsh.erp.datasource.mappers.TaskProcessesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaskProcessesService {
    private Logger logger = LoggerFactory.getLogger(TaskProcessesService.class);
    @Resource
    private TaskProcessesMapper taskProcessesMapper;

}
