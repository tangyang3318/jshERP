package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.annotations.ResultMap;
import org.mapstruct.BeanMapping;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 任务耗材
 *
 */

@TableName(value = "task_material", resultMap = "com.jsh.erp.datasource.mappers.TaskMaterialMapper.BaseResultMap")
public class TaskMaterial {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //任务id
    private Long taskId;
    //商品ID
    private Long materialId;
    //需要物料
    private BigDecimal materialNeedNumber;
    //已有数量
    private BigDecimal materialHasNumber;
    //获取物料
    private BigDecimal materialGetNumber;
    //用料
    private BigDecimal materialUseNumber;
    //废料
    private BigDecimal materialLostNumber;
    //退料
    private BigDecimal materialReturnNumber;
    //租户
    @TableField(exist = false)
    private String tenantId;
    private String barCode;
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
    //商品
    @TableField(exist = false)
    private Material materialEntity;

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

    public BigDecimal getMaterialNeedNumber() {
        return materialNeedNumber;
    }

    public void setMaterialNeedNumber(BigDecimal materialNeedNumber) {
        this.materialNeedNumber = materialNeedNumber;
    }

    public BigDecimal getMaterialHasNumber() {
        return materialHasNumber;
    }

    public void setMaterialHasNumber(BigDecimal materialHasNumber) {
        this.materialHasNumber = materialHasNumber;
    }

    public BigDecimal getMaterialGetNumber() {
        return materialGetNumber;
    }

    public void setMaterialGetNumber(BigDecimal materialGetNumber) {
        this.materialGetNumber = materialGetNumber;
    }

    public BigDecimal getMaterialUseNumber() {
        return materialUseNumber;
    }

    public void setMaterialUseNumber(BigDecimal materialUseNumber) {
        this.materialUseNumber = materialUseNumber;
    }

    public BigDecimal getMaterialLostNumber() {
        return materialLostNumber;
    }

    public void setMaterialLostNumber(BigDecimal materialLostNumber) {
        this.materialLostNumber = materialLostNumber;
    }

    public BigDecimal getMaterialReturnNumber() {
        return materialReturnNumber;
    }

    public void setMaterialReturnNumber(BigDecimal materialReturnNumber) {
        this.materialReturnNumber = materialReturnNumber;
    }


    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    public Material getMaterialEntity() {
        return materialEntity;
    }

    public void setMaterialEntity(Material materialEntity) {
        this.materialEntity = materialEntity;
    }
}
