package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 任务工序
 */
@Data
@TableName(value = "task_processes", resultMap = "com.jsh.erp.datasource.mappers.TaskProcessesMapper.BaseTaskMap")
public class TaskProcesses {
    @TableId(value = "id")
    private Long id;
    //任务id
    private Long taskId;
    //任务单号
    private String billNo;
    //任务单号
    private String barCode;
    //商品ID
    private Long userId;
    //父工序id
    private Long parentProcesses;
    //前序节点
    private String beforeProcesses;
    //工序名称
    private String processesName;
    //创建时间
    private Date createTime;
    //创建时间
    private Date overTime;
    //创建时间
    private Date planOverTime;
    //租户
    @TableField(exist = false)
    private Long tenantId;
    //状态，0未完成、1未验收、2已验收
    private String status;
    //是否是模板
    private String template;
    //创建人
    private Long creator;
    //备注
    private String remark;
    @TableField(exist = false)
    private List<TaskProcesses> taskProcessesList;
    @TableField(exist = false)
    private TaskProcesses parentProcessesEntity;
    @TableField(exist = false)
    private List<TaskProcesses> beforeProcessesEntity;
    @TableField(exist = false)
    private Task task;
    //租户
    @TableField(exist = false)
    private String searchKey;
}
