package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsh.erp.datasource.entities.Task;
import com.jsh.erp.datasource.entities.TaskMaterial;
import com.jsh.erp.service.task.TaskService;
import com.jsh.erp.service.taskMaterial.TaskMaterialService;
import com.jsh.erp.utils.ErpInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
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
@RequestMapping(value = "/taskMaterial")
@Api(tags = {"任务材料"})
public class TaskMaterialController {
    private Logger logger = LoggerFactory.getLogger(TaskMaterialController.class);

    @Resource
    private TaskMaterialService taskMaterialService;
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
        IPage<TaskMaterial> taskMaterialList = taskMaterialService.getListByPage(taskId,pageNo,pageSize);
        objectMap.put("data", taskMaterialList);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * 新增用料
     * @return
     */
    @PostMapping(value = "/insertTaskMaterial")
    @ApiOperation(value = "新增用料")
    public String insertTaskMaterial(@RequestBody TaskMaterial taskMaterial) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        taskMaterialService.insertTaskMaterial(taskMaterial);
        objectMap.put("data",taskMaterial);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * 新增用料
     * @return
     */
    @PostMapping(value = "/updateTaskMaterial")
    @ApiOperation(value = "新增用料")
    public String updateTaskMaterial(@RequestBody TaskMaterial taskMaterial) throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        taskMaterialService.updateTaskMaterial(taskMaterial);
        objectMap.put("data",taskMaterial);
        return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * 领料接口
     * @return
     */
    @PostMapping(value = "/getMaterial")
    @ApiOperation(value = "领料")
    public String getMaterial(@RequestBody JSONObject jsonObject) throws Exception {
        taskMaterialService.getMaterial(jsonObject);
        return returnJson(new HashMap<>(), ErpInfo.OK.name, ErpInfo.OK.code);
    }


    /**
     * 领料接口
     * @return
     */
    @PostMapping(value = "/warehousingProduct")
    @ApiOperation(value = "材料入库")
    public String warehousingProduct(@RequestBody JSONObject jsonObject) throws Exception {
        taskMaterialService.warehousingProduct(jsonObject);
        return returnJson(new HashMap<>(), ErpInfo.OK.name, ErpInfo.OK.code);
    }


    /**
     * 用料
     * @return
     */
    @PostMapping(value = "/useMaterial")
    @ApiOperation(value = "一键用料")
    public String returnMaterial(@RequestBody List<Long> ids) throws Exception {
        taskMaterialService.useMaterial(ids);
        return returnJson(new HashMap<>(), ErpInfo.OK.name, ErpInfo.OK.code);
    }

    /**
     * 用料
     * @return
     */
    @PostMapping(value = "/useMaterialByNumber")
    @ApiOperation(value = "用料")
    public String useMaterialByNumber(@RequestBody JSONObject jsonObject) throws Exception {
        taskMaterialService.useMaterialByNumber(jsonObject);
        return returnJson(new HashMap<>(), ErpInfo.OK.name, ErpInfo.OK.code);
    }
}
