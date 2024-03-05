package com.jsh.erp.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangyang
 */
@RestController
@RequestMapping(value = "/taskProcesses")
@Api(tags = {"任务工序"})
public class TaskProcessesController {
    private Logger logger = LoggerFactory.getLogger(TaskProcessesController.class);
}
