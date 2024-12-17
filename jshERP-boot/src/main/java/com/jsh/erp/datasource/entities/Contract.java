package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("contract")
public class Contract {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //合同名称
    private String contractName;
    //合同编号
    private String contractCode;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //状态状态，状态，0启用、1停用、2删除
    private String status;
    //创建人
    private Long creator;
    //备注
    private String remark;
    //租户
    private Long tenantId;
    //文件
    private String fileName;

    //耗材
    @TableField(exist = false)
    private List<TaskMaterial> taskMaterialList;

    //工序
    @TableField(exist = false)
    private List<TaskProcesses> taskProcessesList;
}
