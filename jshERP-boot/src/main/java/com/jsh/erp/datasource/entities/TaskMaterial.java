package com.jsh.erp.datasource.entities;

import java.util.Date;

public class TaskMaterial {
    private Long id;
    //任务id
    private Long taskId;
    //商品ID
    private Long materialId;
    //需要物料
    private Long materialNeedNumber;
    //已有数量
    private Long materialHasNumber;
    //获取物料
    private Long materialGetNumber;
    //用料
    private Long materialUseNumber;
    //废料
    private Long materialLostNumber;
    //退料
    private Long materialReturnNumber;
    //租户
    private String tenantId;
    //创建时间
    private Date createTime;
    //状态状态，0未审核、1已审核、2加工中、3已完工
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

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialNeedNumber() {
        return materialNeedNumber;
    }

    public void setMaterialNeedNumber(Long materialNeedNumber) {
        this.materialNeedNumber = materialNeedNumber;
    }

    public Long getMaterialHasNumber() {
        return materialHasNumber;
    }

    public void setMaterialHasNumber(Long materialHasNumber) {
        this.materialHasNumber = materialHasNumber;
    }

    public Long getMaterialGetNumber() {
        return materialGetNumber;
    }

    public void setMaterialGetNumber(Long materialGetNumber) {
        this.materialGetNumber = materialGetNumber;
    }

    public Long getMaterialUseNumber() {
        return materialUseNumber;
    }

    public void setMaterialUseNumber(Long materialUseNumber) {
        this.materialUseNumber = materialUseNumber;
    }

    public Long getMaterialLostNumber() {
        return materialLostNumber;
    }

    public void setMaterialLostNumber(Long materialLostNumber) {
        this.materialLostNumber = materialLostNumber;
    }

    public Long getMaterialReturnNumber() {
        return materialReturnNumber;
    }

    public void setMaterialReturnNumber(Long materialReturnNumber) {
        this.materialReturnNumber = materialReturnNumber;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
