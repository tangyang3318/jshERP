package com.jsh.erp.service.task;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.datasource.entities.TaskEx;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "task_component")
@TaskResource
public class TaskComponent implements ICommonQuery {

    @Resource
    private TaskService taskService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return taskService.getTask(id);
    }

    @Override
    public List<?> select(Map<String, String> map) throws Exception {
        return getTaskList(map);
    }

    private List<?> getTaskList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        TaskEx taskEx = new TaskEx();
        taskEx.setBillNo(StringUtil.getInfo(search, "billNo"));
        taskEx.setStatus(StringUtil.getInfo(search, "status"));
        taskEx.setStatus(StringUtil.getInfo(search, "remark"));
        taskEx.setMaterialId(StringUtils.isEmpty(StringUtil.getInfo(search, "materialId")) ? null: Long.parseLong(StringUtil.getInfo(search, "materialId")));
        String planBeginTime = StringUtil.getInfo(search, "planBeginTime");
        String planEndTime = StringUtil.getInfo(search, "planEndTime");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        return taskService.select(taskEx, planBeginTime, planEndTime,beginTime,endTime, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        TaskEx taskEx = new TaskEx();
        taskEx.setBillNo(StringUtil.getInfo(search, "billNo"));
        taskEx.setStatus(StringUtil.getInfo(search, "status"));
        taskEx.setStatus(StringUtil.getInfo(search, "remark"));
        taskEx.setMaterialId(StringUtils.isEmpty(StringUtil.getInfo(search, "materialId")) ? null: Long.parseLong(StringUtil.getInfo(search, "materialId")));
        String planBeginTime = StringUtil.getInfo(search, "planBeginTime");
        String planEndTime = StringUtil.getInfo(search, "planEndTime");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        return taskService.countTask(taskEx, planBeginTime, planEndTime,beginTime,endTime);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception{
        return 0;
//        return taskService.insertTask(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return 0;
//        return taskService.updateTask(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
//        return taskService.deleteTask(id, request);
        return 0;
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
//        return taskService.batchDeleteTask(ids, request);
        return 0;
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
//        return taskService.checkIsNameExist(id, name);
        return 0;
    }

}
