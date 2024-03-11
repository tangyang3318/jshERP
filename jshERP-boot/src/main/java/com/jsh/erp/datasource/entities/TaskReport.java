package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 任务汇报
 *
 */
@TableName(value = "task_report", resultMap = "com.jsh.erp.datasource.mappers.TaskReportMapper.BaseResultMap")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessesId() {
        return processesId;
    }

    public void setProcessesId(Long processesId) {
        this.processesId = processesId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(Long checkUserId) {
        this.checkUserId = checkUserId;
    }

    public BigDecimal getOkNumber() {
        return okNumber;
    }

    public void setOkNumber(BigDecimal okNumber) {
        this.okNumber = okNumber;
    }

    public BigDecimal getLostNumber() {
        return lostNumber;
    }

    public void setLostNumber(BigDecimal lostNumber) {
        this.lostNumber = lostNumber;
    }

    public BigDecimal getScrapNumber() {
        return scrapNumber;
    }

    public void setScrapNumber(BigDecimal scrapNumber) {
        this.scrapNumber = scrapNumber;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getUseDay() {
        return useDay;
    }

    public void setUseDay(int useDay) {
        this.useDay = useDay;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
