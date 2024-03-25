package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jsh.erp.datasource.entities.TaskProcesses;
import com.jsh.erp.datasource.entities.TaskProcessesEX;
import com.jsh.erp.service.taskProcesses.TaskProcessesService;
import com.jsh.erp.utils.ErpInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 删除
     * @return
     */
    @PostMapping(value = "/deleteTaskProcesses")
    @ApiOperation(value = "删除")
    public String deleteTaskProcesses(@RequestBody List ids) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        taskProcessesService.deleteTaskProcesses(ids);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }


    /**
     * 查询所需工序树形
     * @return
     */
    @PostMapping(value = "/searchTaskProcessesTree")
    @ApiOperation(value = "查询所需工序树形")
    public String searchTaskProcessesTree(@RequestBody TaskProcesses taskProcesses) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        List<TaskProcesses> list = taskProcessesService.searchTaskProcessesTree(taskProcesses);
        objectMap.put("data",list);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * 查询所需工序
     * @return
     */
    @PostMapping(value = "/searchTaskProcesses")
    @ApiOperation(value = "查询所需工序")
    public String searchTaskProcesses(@RequestBody TaskProcessesEX taskProcessesEX) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("data",taskProcessesService.searchTaskProcesses(taskProcessesEX,taskProcessesEX.getPageNo(),taskProcessesEX.getPageSize()));
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }


    /**
     * 查询模板
     * @returnT
     */
    @PostMapping(value = "/searchTaskProcessesTempByBarCode")
    @ApiOperation(value = "查询模板")
    public String searchTaskProcessesTempByBarCode(@RequestBody JSONObject jsonObject) throws Exception {
        String barCord = jsonObject.getString("barCode");
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("data",taskProcessesService.searchTaskProcessesTempByBarCode(barCord));
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }



    /**
     * 获取我的工序完成情况
     * @return
     */
    @GetMapping(value = "/getMyTaskProcessesMessage")
    @ApiOperation(value = "获取我的工序完成情况")
    public String getMyTaskProcessesMessage() throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("data",taskProcessesService.getTaskFinishMessage());
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }


    /**
     * 获取任务完成情况
     * @return
     */
    @GetMapping(value = "/getTaskPostponeList")
    @ApiOperation(value = "获取任务完成情况")
    public String getTaskPostponeList() throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("data",taskProcessesService.getTaskPostponeList());
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }
}
