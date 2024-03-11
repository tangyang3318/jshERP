package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsh.erp.datasource.entities.TaskProcesses;
import com.jsh.erp.datasource.entities.TaskReport;
import com.jsh.erp.service.taskProcesses.TaskProcessesService;
import com.jsh.erp.service.taskReport.TaskReportService;
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
import java.util.Map;

import static com.jsh.erp.utils.ResponseJsonUtil.returnJson;

/**
 * @author tangyang
 */
@RestController
@RequestMapping(value = "/taskReport")
@Api(tags = {"任务验收表"})
public class TaskReportController {
    private Logger logger = LoggerFactory.getLogger(TaskReportController.class);

    @Resource
    private TaskReportService taskReportService;
    /**
     * 新增
     * @return
     */
    @PostMapping(value = "/insertTaskReport")
    @ApiOperation(value = "新增验收记录")
    public String insertTaskReport(@RequestBody TaskReport taskReport) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        taskReportService.insertTaskReport(taskReport);
        objectMap.put("data",taskReport);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * 分页获取数据
     * @return
     */
    @PostMapping(value = "/getListByPage")
    @ApiOperation(value = "分页获取数据")
    public String getListByPage(@RequestBody JSONObject jsonObject) throws Exception {
        Integer pageNo = jsonObject.getInteger("pageNo");
        Integer pageSize = jsonObject.getInteger("pageSize");
        Long taskId = jsonObject.getLong("taskId");
        Long processesId = jsonObject.getLong("processesId");
        Map<String, Object> objectMap = new HashMap<String, Object>();
        IPage<TaskReport> taskMaterialList = taskReportService.getListByPage(taskId,processesId,pageNo,pageSize);
        objectMap.put("data", taskMaterialList);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
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
        Integer count = taskReportService.getCanWarehousing(taskId);
        objectMap.put("data", count);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }
}
