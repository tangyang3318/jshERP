package com.jsh.erp.datasource.entities;

import java.math.BigDecimal;
import java.util.Date;

public class Task {
    private Long id;
    //任务单号
    private String billNo;
    //商品ID
    private Long materialId;
    //生产数量
    private String quantity;
    //实际结束时间
    private Date overTime;
    //计划结束时间
    private Date planOverTime;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //状态状态，0未审核、1已审核、2加工中、3已完工
    private String status;
    //创建人
    private Long creator;
    //备注
    private String remark;
    //租户
    private Long tenantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
