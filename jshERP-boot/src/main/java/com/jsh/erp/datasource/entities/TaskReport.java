package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 任务汇报
 *
 */
@TableName(value = "task_report", resultMap = "com.jsh.erp.datasource.mappers.TaskReportMapper.BaseResultMap")
@Data
public class TaskReport {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //工序id
    private Long processesId;
    //任务id
    private Long taskId;
    //加工人id
    private Long userId;
    //校验人id
    private Long checkUserId;
    //合格数量
    private BigDecimal okNumber;
    //报废数量
    private BigDecimal lostNumber;
    //报废数量
    private BigDecimal scrapNumber;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //花费天数
    private int useDay;
    //租户
    private Long tenantId;
    //租户
    private Date createTime;
    //创建人
    private Long creator;
    //备注
    private String remark;
    //文件名称
    private String fileName;

    //入库仓库id
    @TableField(exist = false)
    private Long depotId;
}
