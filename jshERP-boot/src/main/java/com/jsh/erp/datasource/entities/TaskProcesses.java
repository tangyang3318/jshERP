package com.jsh.erp.datasource.entities;

import java.util.Date;

/**
 * 任务工序
 */
public class TaskProcesses {
    private Long id;
    //任务id
    private Long taskId;
    //商品ID
    private Long userId;
    //需要物料
    private Long parentProcesses;
    //已有数量
    private String beforeProcesses;
    //获取物料
    private String processesName;
    //创建时间
    private Date createTime;
    //创建时间
    private Date overTime;
    //创建时间
    private Date planOverTime;
    //租户
    private Long tenantId;
    //状态，0未完成、1未验收、2已验收
    private String status;
    //是否是模板
    private String template;
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

    public Long getParentProcesses() {
        return parentProcesses;
    }

    public void setParentProcesses(Long parentProcesses) {
        this.parentProcesses = parentProcesses;
    }

    public String getBeforeProcesses() {
        return beforeProcesses;
    }

    public void setBeforeProcesses(String beforeProcesses) {
        this.beforeProcesses = beforeProcesses;
    }

    public String getProcessesName() {
        return processesName;
    }

    public void setProcessesName(String processesName) {
        this.processesName = processesName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    public Date getPlanOverTime() {
        return planOverTime;
    }

    public void setPlanOverTime(Date planOverTime) {
        this.planOverTime = planOverTime;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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
