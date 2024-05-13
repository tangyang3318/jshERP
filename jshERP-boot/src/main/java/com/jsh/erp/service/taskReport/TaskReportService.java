package com.jsh.erp.service.taskReport;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.Task;
import com.jsh.erp.datasource.entities.TaskProcesses;
import com.jsh.erp.datasource.entities.TaskReport;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.mappers.TaskMapper;
import com.jsh.erp.datasource.mappers.TaskReportMapper;
import com.jsh.erp.service.task.TaskService;
import com.jsh.erp.service.taskMaterial.TaskMaterialService;
import com.jsh.erp.service.taskProcesses.TaskProcessesService;
import com.jsh.erp.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TaskReportService {
    private Logger logger = LoggerFactory.getLogger(TaskReportService.class);
    @Resource
    private TaskReportMapper taskReportMapper;
    @Resource
    private TaskService taskService;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private UserService userService;
    @Resource
    private TaskProcessesService taskProcessesService;
    @Resource
    private TaskMaterialService taskMaterialService;

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public  void insertTaskReport(TaskReport taskReport) throws Exception {
        // 1. 新增汇报
        if(taskReport == null || taskReport.getOkNumber() == null || taskReport.getCheckUserId() == null){
            return;
        }
        taskReport.setCreateTime(new Date());
        User userInfo=userService.getCurrentUser();
        taskReport.setCreator(userInfo.getId());
        if(taskReport.getCreateTime() == null){
            taskReport.setCreateTime(new Date());
        }
        taskReportMapper.insert(taskReport);
        // 2. 判断如果是任务验收，那么添加完成数量
        if(taskReport.getTaskId() != null && taskReport.getProcessesId() == null){
            Task task = taskService.getTask(taskReport.getTaskId());
            if(task.getOverQuantity() == null){
                task.setOverQuantity(taskReport.getOkNumber());
            }else{
                task.setOverQuantity(task.getOverQuantity().add(taskReport.getOkNumber()));
            }
            task.setStatus(BusinessConstants.TASK_STATE_STATUS_SKIPED);
            taskMapper.updateByPrimaryKeySelective(task);
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

    public void deleteByTaskIds(List<Long> ids) {
        if(!CollectionUtils.isEmpty(ids)){
            QueryWrapper<TaskReport> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("task_id",ids);
            taskReportMapper.delete(queryWrapper);
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void acceptanceCheck(TaskReport taskReport) throws Exception {
        // 1. 新增汇报
        if(taskReport == null || taskReport.getOkNumber() == null || taskReport.getCheckUserId() == null){
            return;
        }
        taskReport.setCreateTime(new Date());
        User userInfo=userService.getCurrentUser();
        taskReport.setCreator(userInfo.getId());
        if(taskReport.getCreateTime() == null){
            taskReport.setCreateTime(new Date());
        }
        taskReportMapper.insert(taskReport);
        // 2. 判断如果是任务验收，那么添加完成数量并且入库
        if(taskReport.getTaskId() != null && taskReport.getProcessesId() == null&& taskReport.getDepotId() != null){
            Task task = taskService.getTask(taskReport.getTaskId());
            if(task.getOverQuantity() == null){
                task.setOverQuantity(taskReport.getOkNumber());
            }else{
                task.setOverQuantity(task.getOverQuantity().add(taskReport.getOkNumber()));
            }
            task.setStatus(BusinessConstants.TASK_STATE_STATUS_END);
            task.setUpdateTime(new Date());
            task.setOverTime(new Date());
            taskMapper.updateByPrimaryKeySelective(task);
            //入库
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("depotId",taskReport.getDepotId());
            jsonObject.put("taskId",taskReport.getTaskId());
            jsonObject.put("barCode",task.getBarCode());
            jsonObject.put("operNumber",taskReport.getOkNumber());
            taskMaterialService.warehousingProduct(jsonObject);
        }else {
            //修改工序状态。
            Long processesId = taskReport.getProcessesId();
            TaskProcesses taskProcesses =  taskProcessesService.getProcessesById(processesId);
            taskProcesses.setStatus(BusinessConstants.PROCESSES_YES_SELECT);
            taskProcesses.setOverTime(new Date());
            taskProcessesService.updateById(taskProcesses);
        }
    }
}
