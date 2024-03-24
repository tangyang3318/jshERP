package com.jsh.erp.service.taskProcesses;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.TaskProcesses;
import com.jsh.erp.datasource.entities.User;
import com.jsh.erp.datasource.mappers.TaskProcessesMapper;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.ProcessesUtils;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@TaskProcessesResource
public class TaskProcessesService implements ICommonQuery {
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

    public List<TaskProcesses> setBeforeProcesses(List<TaskProcesses> taskProcessesList){
        // 1. 遍历获取所有的前置节点id.
        if(CollectionUtils.isEmpty(taskProcessesList)){
            return null;
        }
        List<Long> ids = new ArrayList<>();
        Map<Long,List<Long>> idMap =new HashMap<>();
        taskProcessesList.forEach(item-> {
            String id = item.getBeforeProcesses();
            if(StringUtil.isNotEmpty(id)){
                String[] split = id.split(",");
                List<Long> nowIds = new ArrayList<>();
                for (String s : split) {
                    ids.add(Long.parseLong(s));
                    nowIds.add(Long.parseLong(s));
                }
                idMap.put(item.getId(),nowIds);
            }
        });
        // 2. 查询所有前置节点
        if(CollectionUtils.isEmpty(ids)){
            return null;
        }
        List<TaskProcesses> processes = taskProcessesMapper.selectBatchIds(ids);
        // 3. 返回所有前置节点集合
        for (TaskProcesses taskProcesses : taskProcessesList) {
            List<Long> longs = idMap.get(taskProcesses.getId());
            if(CollectionUtils.isEmpty(longs)){
                break;
            }
            List<TaskProcesses> collect = processes.stream().filter(item -> longs.contains(item.getId())).collect(Collectors.toList());
            taskProcesses.setBeforeProcessesEntity(collect);
        }
        return taskProcessesList;
    }

    public Page<TaskProcesses> searchTaskProcesses(TaskProcesses taskProcesses, Integer pageNo, Integer pageSize) {
        IPage<TaskProcesses> page = new Page();
        Page<TaskProcesses> returnList = new Page<>();
        if(pageNo != null && pageSize != null){
            page = new Page(pageNo,pageSize);
            returnList = taskProcessesMapper.searchTaskProcesses(page,taskProcesses);
        }else{
            List<TaskProcesses> processes = taskProcessesMapper.searchTaskProcesses(taskProcesses);
            //填充前序节点
            returnList.setRecords(processes);
            returnList.setTotal(processes.size());
            returnList.setCurrent(1);
        }
        //设置前序节点
        returnList.setRecords(setBeforeProcesses(returnList.getRecords()));
        //填充
        return returnList;
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

    @Override
    public TaskProcesses selectOne(Long id) throws Exception {
        return taskProcessesMapper.selectById(id);
    }

    @Override
    public List<?> select(Map<String, String> parameterMap) throws Exception {
        IPage<TaskProcesses> taskList = getTaskList(parameterMap);
        return taskList.getRecords();
    }

    private IPage<TaskProcesses> getTaskList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        Page<TaskProcesses> page = new Page<>(QueryUtils.offset(map), QueryUtils.rows(map));
        String taskId = StringUtil.getInfo(search, "taskId");
        String userId = StringUtil.getInfo(search, "userId");
        String planBeginTime = StringUtil.getInfo(search, "planBeginTime");
        String planEndTime = StringUtil.getInfo(search, "planEndTime");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        String status = StringUtil.getInfo(search, "status");
        LambdaQueryWrapper<TaskProcesses> queryWrapper = new LambdaQueryWrapper();
        if(StringUtil.isNotEmpty(taskId)){
            queryWrapper.ge(TaskProcesses::getTaskId,Long.parseLong(taskId));
        }
        if(StringUtil.isNotEmpty(userId)){
            queryWrapper.ge(TaskProcesses::getUserId,Long.parseLong(userId));
        }
        if(StringUtil.isNotEmpty(status)){
            queryWrapper.ge(TaskProcesses::getStatus,status);
        }
        if(StringUtil.isNotEmpty(planBeginTime) && StringUtil.isNotEmpty(planEndTime)){
            queryWrapper.between(TaskProcesses::getPlanOverTime,planBeginTime,planEndTime);
        }
        if(StringUtil.isNotEmpty(beginTime) && StringUtil.isNotEmpty(endTime)){
            queryWrapper.between(TaskProcesses::getOverTime,beginTime,endTime);
        }
        return taskProcessesMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long counts(Map<String, String> parameterMap) throws Exception {
        IPage<TaskProcesses> taskList = getTaskList(parameterMap);
        return taskList.getTotal();
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception {
        return 0;
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request) throws Exception {
        return 0;
    }

    @Override
    public int delete(Long id, HttpServletRequest request) throws Exception {
        return 0;
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request) throws Exception {
        return 0;
    }

    @Override
    public int checkIsNameExist(Long id, String name) throws Exception {
        return 0;
    }
}
