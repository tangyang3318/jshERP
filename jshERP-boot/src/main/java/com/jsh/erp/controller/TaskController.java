package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.datasource.entities.Account;
import com.jsh.erp.datasource.vo.AccountVo4InOutList;
import com.jsh.erp.datasource.vo.AccountVo4List;
import com.jsh.erp.service.account.AccountService;
import com.jsh.erp.service.systemConfig.SystemConfigService;
import com.jsh.erp.utils.BaseResponseInfo;
import com.jsh.erp.utils.ErpInfo;
import com.jsh.erp.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    /**
     * 获取所有任务
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping(value = "/getAllTask")
    @ApiOperation(value = "获取所有任务")
    public String getAllTask(@RequestBody JSONObject jsonObject,
                                 HttpServletRequest request)throws Exception {
        return null;
    }
}
