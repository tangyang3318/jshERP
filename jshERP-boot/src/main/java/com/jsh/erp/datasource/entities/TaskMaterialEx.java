package com.jsh.erp.datasource.entities;

import lombok.Data;

/**
 * Description
 *
 * @Author: cjl
 * @Date: 2019/2/25 11:40
 */
@Data
public class TaskMaterialEx extends TaskMaterial{
    private Integer pageNo;
    private Integer pageSize;
}
