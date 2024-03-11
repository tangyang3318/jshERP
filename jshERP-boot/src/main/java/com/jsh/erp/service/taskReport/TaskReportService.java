package com.jsh.erp.service.taskReport;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.Task;
import com.jsh.erp.datasource.entities.TaskReport;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.mappers.TaskReportMapper;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.service.task.TaskService;
import com.jsh.erp.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TaskReportService {
    private Logger logger = LoggerFactory.getLogger(TaskReportService.class);
    @Resource
    private TaskReportMapper taskReportMapper;
    @Resource
    private TaskService taskService;
    @Resource
    private UserService userService;

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public  void insertTaskReport(TaskReport taskReport) throws Exception {
        // 1. 新增汇报
        taskReport.setCreateTime(new Date());
        User userInfo=userService.getCurrentUser();
        taskReport.setCreator(userInfo.getId());
        taskReportMapper.insert(taskReport);
        // 2. 判断如果是任务验收，那么添加完成数量
        if(taskReport.getTaskId() != null && taskReport.getProcessesId() == null){
            Task task = taskService.getTask(taskReport.getTaskId());
            task.setOverQuantity(task.getOverQuantity().add(taskReport.getOkNumber()));
            taskService.updateTask(task);
        }
    }

    public IPage<TaskReport> getListByPage(Long taskId, Long processesId, Integer pageNo, Integer pageSize) {
        IPage page = new Page();
        if(pageNo != null && pageSize != null){
            page = new Page(pageNo,pageSize);
        }
        QueryWrapper<TaskReport> queryWrapper = new QueryWrapper<>();
        if(taskId != null){
            queryWrapper.eq("task_id",taskId);
        }
        if(processesId != null){
            queryWrapper.eq("processes_id",processesId);
        }
        IPage page1 = taskReportMapper.selectPage(page, queryWrapper);
        return page1;
    }

    public Integer getCanWarehousing(Long taskId) {
        QueryWrapper<TaskReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        queryWrapper.isNull("processes_id");
        return taskReportMapper.selectCount(queryWrapper);
    }
}
