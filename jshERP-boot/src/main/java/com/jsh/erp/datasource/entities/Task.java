package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Task {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //任务单号
    private String billNo;

    private String barCode;
    //商品ID
    private Long materialId;
    //目标生产数量
    private BigDecimal quantity;
    //生产数量
    private BigDecimal overQuantity;
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
    //文件名称
    private String fileName;

    //耗材
    @TableField(exist = false)
    private List<TaskMaterial> taskMaterialList;

    //工序
    @TableField(exist = false)
    private List<TaskProcesses> taskProcessesList;
}
