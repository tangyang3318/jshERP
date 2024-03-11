package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.datasource.entities.Task;
import com.jsh.erp.service.task.TaskService;
import com.jsh.erp.utils.ErpInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jsh.erp.utils.ResponseJsonUtil.returnJson;

/**
 * @author tangyang
 */
@RestController
@RequestMapping(value = "/task")
@Api(tags = {"任务管理"})
public class TaskController {
    private Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Resource
    private TaskService taskService;
    /**
     * 批量删除
     * @param longs
     * @return
     */
    @PostMapping(value = "/deleteByIds")
    @ApiOperation(value = "批量删除")
    public String deleteByIds(@RequestBody List<Long> longs) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        int delete = taskService.deleteTaskByIds(longs);
        if(delete > 0) {
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else if(delete == -1) {
            return returnJson(objectMap, ErpInfo.TEST_USER.name, ErpInfo.TEST_USER.code);
        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }


    /**
     * 新增
     * @return
     */
    @PostMapping(value = "/insertTask")
    @ApiOperation(value = "新增任务")
    public String insertTask(@RequestBody Task task) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        int insertTask = taskService.insertTask(task);
        if(insertTask > 0) {
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else if(insertTask == -1) {
            return returnJson(objectMap, ErpInfo.TEST_USER.name, ErpInfo.TEST_USER.code);
        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }
    /**
     * 编辑
     * @return
     */
    @PostMapping(value = "/updateTask")
    @ApiOperation(value = "编辑")
    public String updateTask(@RequestBody Task task) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        int insertTask = taskService.updateTask(task);
        if(insertTask > 0) {
            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
        } else if(insertTask == -1) {
            return returnJson(objectMap, ErpInfo.TEST_USER.name, ErpInfo.TEST_USER.code);
        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
        }
    }


    /**
     * 获取可入库数量
     * @return
     */
    @PostMapping(value = "/getCanWarehousing")
    @ApiOperation(value = "分页获取数据")
    public String getCanWarehousing(@RequestBody JSONObject jsonObject) throws Exception {
        Long taskId = jsonObject.getLong("taskId");
        Map<String, Object> objectMap = new HashMap<String, Object>();
        BigDecimal count = taskService.getCanWarehousing(taskId);
        objectMap.put("data", count);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * 任务完工
     * @return
     */
    @PostMapping(value = "/overTask")
    @ApiOperation(value = "任务完工")
    public String overTask(@RequestBody JSONObject jsonObject) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        Long taskId = jsonObject.getLong("taskId");
        taskService.overTask(taskId);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }
}
