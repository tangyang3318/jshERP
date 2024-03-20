package com.jsh.erp.datasource.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 *
 * @Author: cjl
 * @Date: 2019/2/25 11:40
 */
@Data
@TableName(value = "task", resultMap = "com.jsh.erp.datasource.mappers.TaskMapperEx.ResultMapEx")
public class TaskEx extends Task {
    //负责人名字
    @TableField(exist = false)
    private String creatorName;
    //商品
    @TableField(exist = false)
    private Material material;
}
