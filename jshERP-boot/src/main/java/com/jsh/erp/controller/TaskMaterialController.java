package com.jsh.erp.controller;

import com.jsh.erp.datasource.entities.Task;
import com.jsh.erp.datasource.entities.TaskMaterial;
import com.jsh.erp.service.task.TaskService;
import com.jsh.erp.service.taskMaterial.TaskMaterialService;
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
    @PostMapping(value = "/getAllList")
    @ApiOperation(value = "获取所有的列表")
    public String getAllList() throws Exception {
        Map<String, Object> objectMap = new HashMap<String, Object>();
//        List<TaskMaterial> taskMaterialList = taskMaterialService.getAllList();
//        if(delete > 0) {
//            objectMap.put("total", taskMaterialList.);
//            objectMap.put("rows", taskMaterialList);
//            return returnJson(objectMap, ErpInfo.OK.name, ErpInfo.OK.code);
//        } else if(delete == -1) {
//            return returnJson(objectMap, ErpInfo.TEST_USER.name, ErpInfo.TEST_USER.code);
//        } else {
            return returnJson(objectMap, ErpInfo.ERROR.name, ErpInfo.ERROR.code);
//        }
    }
}
