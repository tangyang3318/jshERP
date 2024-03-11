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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
        taskProcesses.setTemplate(BusinessConstants.IS_NOT_TEMPLETE);
        taskProcessesMapper.insert(taskProcesses);
        System.out.println(taskProcesses);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateTaskProcesses(TaskProcesses taskProcesses) {
        taskProcessesMapper.updateById(taskProcesses);
    }
}
