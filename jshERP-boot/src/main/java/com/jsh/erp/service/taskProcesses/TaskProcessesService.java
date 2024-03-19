package com.jsh.erp.service.taskProcesses;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.TaskProcesses;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.mappers.TaskProcessesMapper;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.ProcessesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskProcessesService {
    private Logger logger = LoggerFactory.getLogger(TaskProcessesService.class);
    @Resource
    private TaskProcessesMapper taskProcessesMapper;
    @Resource
    private UserService userService;

    public IPage<TaskProcesses> getListByPage(Long taskId,Integer pageNo, Integer pageSize) {
        IPage page = new Page();
        if(pageNo != null && pageSize != null){
            page = new Page(pageNo,pageSize);
        }
        QueryWrapper<TaskProcesses> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",taskId);
        IPage page1 = taskProcessesMapper.selectPage(page, queryWrapper);
        return page1;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insertTaskProcesses(TaskProcesses taskProcesses) throws Exception {
        taskProcesses.setStatus(BusinessConstants.PROCESSES_NO_COM);
        taskProcesses.setCreateTime(new Date());
        User userInfo=userService.getCurrentUser();
        taskProcesses.setCreator(userInfo.getId());
        if(taskProcesses.getTemplate() == null || taskProcesses.getTemplate().equals(BusinessConstants.IS_NOT_TEMPLETE)){
            taskProcesses.setTemplate(BusinessConstants.IS_NOT_TEMPLETE);
        }else{
            taskProcesses.setTemplate(BusinessConstants.IS_TEMPLETE);
        }

        taskProcessesMapper.insert(taskProcesses);
        System.out.println(taskProcesses);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateTaskProcesses(TaskProcesses taskProcesses) {
        taskProcessesMapper.updateById(taskProcesses);
    }

    public void deleteTaskProcesses(List ids) {
        if(!CollectionUtils.isEmpty(ids)){
            taskProcessesMapper.deleteBatchIds(ids);
        }
    }

    public Page<TaskProcesses> searchTaskProcesses(TaskProcesses taskProcesses, Integer pageNo, Integer pageSize) {
        IPage<TaskProcesses> page = new Page();
        if(pageNo != null && pageSize != null){
            page = new Page(pageNo,pageSize);
            return taskProcessesMapper.searchTaskProcesses(page,taskProcesses);
        }else{
            Page<TaskProcesses> returnList = new Page<>();
            List<TaskProcesses> processes = taskProcessesMapper.searchTaskProcesses(taskProcesses);
            returnList.setRecords(processes);
            returnList.setTotal(processes.size());
            returnList.setCurrent(1);
            return returnList;
        }
    }

    public List<TaskProcesses> searchTaskProcessesTree(TaskProcesses taskProcesses) {
        List<TaskProcesses> collect = new ArrayList<>();
        List<TaskProcesses> processesList = taskProcessesMapper.searchTaskProcesses(taskProcesses);
        if(!CollectionUtils.isEmpty(processesList)){
            collect = processesList.stream().map(item -> {
                item.setTaskProcessesList(ProcessesUtils.getChildList(processesList, item.getId()));
                return item;
            }).filter(item -> item.getParentProcesses().longValue() == 0).collect(Collectors.toList());
        }
        return collect;
    }

    public Map<String,Integer> getTaskFinishMessage() throws Exception {
        Map<String,Integer> returnmap = new HashMap<>();
        // 获取
        User userInfo=userService.getCurrentUser();
        QueryWrapper<TaskProcesses> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template",BusinessConstants.IS_NOT_TEMPLETE)
        .eq("user_id",userInfo.getId());
        List<TaskProcesses> tasks = taskProcessesMapper.selectList(queryWrapper);
        returnmap.put("taskProcessesTotalNumber",tasks.size());
        Integer notFinishNumber = 0 , finishNumberNumber = 0
                ,postponeNumber = 0,onScheduleNumber = 0;
        for (TaskProcesses task : tasks) {
            if(task.getStatus().equals(BusinessConstants.TASK_STATE_STATUS_END)){
                finishNumberNumber++;
                //已完工的。
                if(task.getOverTime().after(task.getPlanOverTime())) {
                    postponeNumber ++;
                }else{
                    onScheduleNumber ++;
                }
            }else{
                notFinishNumber ++;
                if(task.getPlanOverTime() != null && new Date().after(task.getPlanOverTime())) {
                    postponeNumber ++;
                }
            }
        }
        returnmap.put("notFinishNumber",notFinishNumber);
        returnmap.put("finishNumberNumber",finishNumberNumber);
        returnmap.put("postponeNumber",postponeNumber);
        returnmap.put("onScheduleNumber",onScheduleNumber);
        return returnmap;
    }

    public Map<String,Object> getTaskPostponeList() {
       return taskProcessesMapper.getTaskPostponeList();
    }
}
