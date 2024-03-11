package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jsh.erp.datasource.entities.TaskProcesses;
import com.jsh.erp.service.taskProcesses.TaskProcessesService;
import com.jsh.erp.utils.ErpInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jsh.erp.utils.ResponseJsonUtil.returnJson;

/**
 * @author tangyang
 */
@RestController
@RequestMapping(value = "/taskProcesses")
@Api(tags = {"任务工序"})
public class TaskProcessesController {
    private Logger logger = LoggerFactory.getLogger(TaskProcessesController.class);


    @Resource
    private TaskProcessesService taskProcessesService;
    /**
     * 批量删除
     * @return
     */
    @PostMapping(value = "/getListByPage")
    @ApiOperation(value = "分页获取数据")
    public String getListByPage(@RequestBody JSONObject jsonObject) throws Exception {
        Integer pageNo = jsonObject.getInteger("pageNo");
        Integer pageSize = jsonObject.getInteger("pageSize");
        Long taskId = jsonObject.getLong("taskId");
        Map<String, Object> objectMap = new HashMap<String, Object>();
        IPage<TaskProcesses> taskProcessesIPage = taskProcessesService.getListByPage(taskId,pageNo,pageSize);
        objectMap.put("data", taskProcessesIPage);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * 新增
     * @return
     */
    @PostMapping(value = "/insertTaskProcesses")
    @ApiOperation(value = "新增工序")
    public String insertTaskProcesses(@RequestBody TaskProcesses taskProcesses) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        taskProcessesService.insertTaskProcesses(taskProcesses);
        objectMap.put("data",taskProcesses);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }
    /**
     * 修改
     * @return
     */
    @PostMapping(value = "/updateTaskProcesses")
    @ApiOperation(value = "修改工序")
    public String updateTaskProcesses(@RequestBody TaskProcesses taskProcesses) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        taskProcessesService.updateTaskProcesses(taskProcesses);
        objectMap.put("data",taskProcesses);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }
}
