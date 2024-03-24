package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.annotations.ResultMap;
import org.mapstruct.BeanMapping;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 任务耗材
 *
 */
@Data
@TableName(value = "task_material", resultMap = "com.jsh.erp.datasource.mappers.TaskMaterialMapper.BaseResultMap")
public class TaskMaterial {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //任务id
    private Long taskId;
    //任务单号
    private String billNo;
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
    //搜索ke'y值
    @TableField(exist = false)
    private String searchKey;
    //商品
    @TableField(exist = false)
    private Material materialEntity;
}
